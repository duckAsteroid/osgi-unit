package com.asteroid.duck.osgi.junit

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction

/**
 * A task that can write out the names of the OSGi bundles (JARs) required to run a test (i.e. the project testRuntime)
 * these JARs are written to a file so that this can be passed to the framework inside the unit tests.
 */
class ExportOSGiBundlesTask extends DefaultTask {
    @OutputFile
    File outputFile;

    @TaskAction
    def exportBundlesFile() {
        // pathTxt will hold the content of our JARs for the runtime
        def pathTxt = ""

        // add the test JAR for this project
        pathTxt += project.testJar.archivePath.absoluteFile.path + "\n"

        // add the main JAR for this project
        pathTxt += project.jar.archivePath.absoluteFile.path + "\n"

        // add each JAR from testRuntime configuration - but not those in the framework
        def bundles = project.configurations.testRuntime - project.configurations.testFramework
        bundles.each {
            pathTxt += path + "\n"
        }

        outputFile.text = pathTxt
    }
}
