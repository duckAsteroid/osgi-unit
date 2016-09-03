package com.asteroid.duck.osgi.junit

import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * A plugin class that adds OSGi JUnit tasks and capabilities to a project
 */
class OSGiJUnitPlugin implements Plugin<Project> {
    void apply(Project project) {
        project.extensions.create("osgi-unit", OSGiJUnitPluginExtension)
        project.task('hello') << {
            println "${project.greeting.message} from ${project.greeting.greeter}"
        }
        project.tasks.create 'export', ExportOSGiBundlesTask.class
    }
}

class OSGiJUnitPluginExtension {
    String message
}