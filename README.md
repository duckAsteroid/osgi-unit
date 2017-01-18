osgi-unit
=========

Unit testing for OSGi projects in Gradle.

To use the library add the following to your build.gradle:
```groovy    
    buildscript {
        repositories {
            // dependencies from MVN central (you can use others)
            mavenCentral()
            maven {
                // adds the maven repo for this plugin
                url "https://dl.bintray.com/osgi-unit/maven"
            }
        }
        dependencies {
            classpath 'com.asteroid.duck:osgi-unit-gradle:1.0.0'
        }
    }
       
    apply plugin: 'com.asteroid.duck.osgi.junit'
```   
This plugin will then let you write JUnit tests in the traditional way
in Gradle (e.g. as JUnit4 compliant `.java` files in the `src/test/java` folder).
However, at runtime these tests can execute inside an OSGi container.

To do this the `osgi-unit` library first creates and initialises an OSGi container for your tests; 
The classpath for this OSGi framework is specified using the `testFramework` dependency configuration. 
After the OSGi framework is started any JARs (including other Gradle projects) from the
`testRuntime` dependency container are added to the framework as bundles and started. As an example, the following 
`dependencies` section in your `build.gradle` will cause the tests to run in Apache Felix container (with JUnit4):

```groovy
    dependencies {
        compile 'org.osgi:org.osgi.core:4.+'
        testCompile 'junit:junit:4.+'
        testFramework 'org.apache.felix:org.apache.felix.framework:5.4.0'
    }
```
> _Note: Dependencies (such as those in `compile`) must be OSGi compliant bundles to work! (for now) Regular JAR files without the 
necessary meta-data will simply fail to load._

In addition to starting the OSGi framework; the plugin will package your test classes into a 'special' JAR file (with the necessary meta-data) so
this too can be deployed into the OSGi container. 

> _Note: The name of the JAR will be the same as your projects main JAR with `-test` appended before the `.jar` 
extension. (e.g. `my-project_1.2.3-test.jar`)_

So now your tests are deployed inside a running OSGi framework, you will want to acquire and use services in them? Here 
you have some options dependent on what you want to achieve.

#### Acquiring Services from OSGi
If you want to acquire instances of OSGi services through the micro services registry you can use the `WithOSGiService`
rule. This rule will acquire and release an instance of the service (using official OSGi mechanisms) during the test 
setup, and tear down phase respectively. You declare the rule as a public field in your test, passing it the service 
interface you seek; For example:

```java
    @Rule
    public WithOSGiService<MySimpleService> service = new WithOSGiService<MySimpleService>(MySimpleService.class);
```
To use the service in your test method you would then call:

```java
    // get the current instance 
    MySimpleService serviceInstance = service.getServiceInstance();
    // call it
    serviceInstance.doStuff();
    // assert etc.
```
#### Using the OSGi Framework directly

Alternatively you can add the `WithOSGi` rule to your classes:
```java
     @Rule
     public WithOSGi osgi = new WithOSGi();
```
This object then provides direct access to the OSGi framework and the test `Bundle` (and it's `BundleContext`). As an 
example:  

```java
     // the OSGi framework object
     Framework framework = osgi.getFramework();
     // the "test" bundle
     Bundle testBundle = osgi.getTestBundle();
     // the BundleContext of the test bundle
     BundleContext ctx = osgi.getTestBundleContext();
```

Known Limitations
-----------------
Sadly your test classes can't be in the same package as the classes being tested 
(yet). Since OSGi (BND) gets very confused about Import/Export packages and it fails.
to resolve the test bundle at runtime. I am sure this is solvable though.

Ideas
------------

 * Add a regular JAR wrapper feature that allows all classes in it to be exported in OSGi
 * Add the ability to control start order of bundles through our plugin extension; and have mandatory started JARs?
 * Make the `WithOSGiService` rule track the service in case it comes/goes during testing
 * Make the `WithOSGiService` rule support exotic filters and service names
 
Project Source Code Structure
-----------------------------
The project is divided into 2 sub-projects:
 * `core` - which contains Java code used by the runtime (and referenced by the plugin for constants etc.)
 * `plugin` - which contains the Groovy code for the Gradle plugin
  
### How does it work?
1. The plugin first replaces the classpath of the `test` task with the `testFramework` JARs (and their dependencies). 
Including the osgi-unit `core` JAR.
2. During the build we grab the `testRuntime` classpath (and dependent JARs) and write this out (so we can load these 
into OSGi later...) 
3. A special "test" JAR (bundle) is created as part of the build, this contains your test classes
3. Upon launching the `test` task; we pass in lots of extra properties to let our JAR know where your test classes are 
what the `testRuntime` path was etc.
4. At launch we swap the `java.system.class.loader` for our own "freaky" classloader. This is the real trick... this 
classloader knows which classes to load from the "boot" classpath (i.e. `testFramework` and the JVM classpath); and 
which to load from OSGi. 
5. When the JUnit test executor tries to load your test class (or any other class outside the 
"boot" classpath) - to do this the freaky class loader will :
   1. Start the OSGi framework
   2. Install the JARs (bundles) - which we squirreled away in step 2 
   3. Start all the bundles
   4. Store a *singleton* reference to the OSGi framework (for later, when your tests run)
   4. Locate the "test" bundle - the JAR we created at step 3
   5. Ask the "test" bundle to load the class (and any classes it needs)
6. Now a class is handed back to JUnit that came from OSGi
7. JUnit creates instances of your class and runs them (as normal)
8. If your JUnit uses our special JUnit *rule* classes, they can access the OSGi framework singleton from step 6 (iv)
9. Your tests can use OSGi stuff like it was running for "normally"
 
Build & Release
----------------
The project source is maintained on [github.com](https://github.com/duckAsteroid/osgi-unit).

The project build is performed by [Travis-CI](https://travis-ci.org/duckAsteroid/osgi-unit), where the current status is: ![Build Status](https://travis-ci.org/duckAsteroid/osgi-unit.svg?branch=master)

The binaries are available from [bintray.com](https://dl.bintray.com/osgi-unit/maven/)
 