package com.asteroid.duck.osgi.junit

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

/**
 * A task that can write out the names of the OSGi bundles (JARs) required to run a test (i.e. the project testRuntime)
 * these JARs are written to a file so that this can be passed to the framework inside the unit tests.
 */
class ExportOSGiBundlesTask extends DefaultTask {
    @TaskAction
    def hello() {
        def file = project.file("$project.buildDir/hello.txt")
        file.parentFile.mkdirs()
        file.write "Hello!"
    }
}
