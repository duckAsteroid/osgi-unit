package com.asteroid.duck.osgi.junit

import com.asteroid.duck.osgi.Constants
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.jvm.tasks.Jar

/**
 * A plugin class that adds OSGi JUnit tasks and capabilities to a project
 */
class OSGiJUnitPlugin implements Plugin<Project> {

    void apply(Project project) {
        project.configure(project) {
            // projects need to be OSGi
            apply plugin: 'osgi'
            // we add an extra configuration to the project for the framework bootstrap path
            configurations {
                testFramework {
                    description = "OSGi framework classpath for tests"
                }
            }

            // add our core artifact to the test framework path and the compile path
            dependencies {
                testCompile Constants.CORE_ARTIFACT_COORD
                testFramework Constants.CORE_ARTIFACT_COORD
            }
        }

        // add our plugin to the project
        project.extensions.create("osgiUnit", OSGiJUnitPluginExtension)

        // create the test bundle symbolic name
        def TESTS = 'tests'
        def testBSN = project.jar.manifest.getSymbolicName() + '.' + TESTS

        // create a JAR for the test classes (with an OSGi manifest)
        project.tasks.create 'testJar' , Jar.class
        project.testJar {
            group 'build'
            description "Assembles a jar archive containing the project test classes & resources"
            classifier = TESTS
            from project.sourceSets.test.output
            dependsOn project.testClasses

            // OSGi manifest for the test JAR
            manifest = project.osgiManifest {
                    classesDir = project.sourceSets.test.output.classesDir
                    classpath = project.sourceSets.test.runtimeClasspath
                instructionReplace 'Bundle-Description', "JUnit tests for :- " + project.description
                instructionReplace 'Bundle-SymbolicName', testBSN
            }
        }

        // add a task to export the compile path JAR names to a file for starting the OSGi framework
        File exportedRuntimeClasspath = new File(project.buildDir, "gradle.testRuntime.path")
        project.tasks.create 'export', ExportOSGiBundlesTask.class
        project.export {
            group 'osgi'
            description = "Writes the OSGi framework boot path (jars) to a file for use by framework"
            dependsOn project.testJar

            // the target file for
            outputFile = exportedRuntimeClasspath
        }
        // Configure the test task to make it run in OSGi...
        project.test {
            def osgiCacheDir = new File(project.buildDir, 'osgi-cache')
            // the classpath for testing is now only the OSGi framework piece
            doFirst {
                String classpathDebug = 'Replacing classpath:=\n'
                for (File path : project.test.classpath) {
                    classpathDebug += '\t' + path.absolutePath + '\n'
                }
                classpathDebug += 'With :=\n'
                for (File path : project.configurations.testFramework) {
                    classpathDebug += '\t' + path.absolutePath + '\n'
                }
                logger.info(classpathDebug)

                classpath = project.configurations.testFramework
                logger.info('Delete and recreate ' + osgiCacheDir.absolutePath)
                osgiCacheDir.delete()
                osgiCacheDir.mkdirs()
            }
            // we need the project jar file
            dependsOn project.jar
            // we need the exported runtime classpath (which JARs to install/start in OSGi)
            dependsOn project.export
            // we need to pass the exported runtime JARs (file) to our bootstrap code
            systemProperty Constants.RUNTIME_BUNDLES, exportedRuntimeClasspath
            // we also need to tell it which bundle contains our tests
            systemProperty Constants.TEST_BUNDLE, testBSN
            // and which packages our classloader should ignore...
            systemProperty Constants.SYSTEM_PACKAGES, "sun.*,com.asteroid.duck.osgi.*"
            // use our classloader (to find test classes and friends)
            systemProperty 'java.system.class.loader', 'com.asteroid.duck.osgi.FreakyClassLoader'
            // use a folder inside the /build directory for the OSGi bundle cache
            systemProperty 'org.osgi.framework.storage', osgiCacheDir.absolutePath
            // always use a fresh bundle cache
            systemProperty 'org.osgi.framework.storage.clean', 'onFirstInit'
            // packages that are exported by the system (i.e. the classpath that loaded the framework)
            systemProperty 'org.osgi.framework.system.packages.extra', "org.junit,org.junit.rules,org.junit.runners,org.junit.runners.model," +
                    "com.asteroid.duck.osgi; version=${Constants.VERSION},com.asteroid.duck.osgi.junit; version=${Constants.VERSION},com.asteroid.duck.osgi.log; version=${Constants.VERSION}"
        }
    }
}
/**
 * osgi-unit extension to the project model
 */
class OSGiJUnitPluginExtension {
    /**
     * Should we use a bundle file or path variable to pass list of bundles to osgi-unit runtime.
     * The bundle file (in build dir) can be useful for debug
     */
    boolean useBundleFile = false
}