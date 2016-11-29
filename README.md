osgi-unit
=========

Unit testing for OSGi projects in Gradle.

To use the library add the following to your build.gradle:
   
    apply plugin: 'com.asteroid.duck.osgi.junit'
   
This plugin will then let you write JUnit tests in the traditional way
in Gradle (e.g. as JUnit4 compliant `.java` files in the `src/test/java` folder).
But at runtime the tests will execute inside an OSGi container.

To do this the osgi-junit library first creates and initialises an OSGi container for your tests; 
The classpath for this OSGi framework is specified using the `testFramework` dependency configuration. 
After the OSGi framework is started any JARs (including other Gradle projects) from the
`testRuntime` dependency container are added to the framework as bundles and started. _Note: Only JAR 
dependencies that are OSGi compliant bundles will work! Regular JAR files without the necessary meta-data
will fail to load._

In addition the plugin will package your test classes into a JAR file (with the necessary meta-data) so
these too can be deployed into the OSGi container. _Note: The name of the JAR will be the same as your projects
main JAR with `-test` appended before the `.jar` extension._

Finally to use the OSGi framework within your test classes you should add
the `WithOSGi` rule to your classes. This object provides access to the OSGi
framework and the `BundleContext` of the test bundle. From here you can use
regular OSGi mechanisms to discover OSGi micro services.

Known Limitations
-----------------
Sadly your test classes can't be in the same package as the classes being tested 
(yet). Since OSGi (BND) gets very confused about Import/Export packages and it fails.
to resolve the test bundle at runtime. I am sure this is solvable though.

Ideas
------------

 * Add a regular JAR wrapper feature that allows all classes in it to be exported in OSGi
 * Add a service rule to acquire/release services during setup/teardown.
 