package com.asteroid.duck.osgi.junit

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.jvm.tasks.Jar

/**
 * A plugin class that adds OSGi JUnit tasks and capabilities to a project
 */
class OSGiJUnitPlugin implements Plugin<Project> {
    static final String GROUP = "com.asteroid.duck";
    static final String ARTIFACT = "osgi-unit-gradle";
    static final String VERSION = "1.0.0";

    void apply(Project project) {
        project.configure(project) {
            apply plugin: 'osgi'
            configurations {
                testFramework {
                    description = "Test OSGi framework classpath"
                }
            }
            dependencies {
                testCompile GROUP + ":" + ARTIFACT + ":" + VERSION
            }
        }
        // add our plugin to the project
        project.extensions.create("osgi-unit", OSGiJUnitPluginExtension)
        // create a fragment JAR for the test classes (with an OSGi manifest)
        project.tasks.create 'testJar' , Jar.class
        project.testJar {
            group 'build'
            description "Assembles a jar archive containing the project test classes & resources"
            classifier = 'tests'
            from project.sourceSets.test.output
            dependsOn project.testClasses
            manifest = project.osgiManifest {
                    classesDir = project.sourceSets.test.output.classesDir
                    classpath = project.sourceSets.test.runtimeClasspath
                    instructionReplace 'Bundle-Description', project.description
                    instructionReplace 'Bundle-SymbolicName', project.jar.name + classifier
                    instructionReplace 'Fragment-Host', project.group + '.' + project.name
            }
        }

        // add a task to export the required JAR names to a file for starting the OSGi framework
        project.tasks.create 'export', ExportOSGiBundlesTask.class
        project.export {
            group 'osgi'
            description = "Writes the OSGi framework boot path (jars) to a file for use by framework"
            dependsOn project.testJar
        }
        project.test {
            classpath = project.configurations.testFramework
            dependsOn project.export
        }
    }
}

class OSGiJUnitPluginExtension {
    String message
}