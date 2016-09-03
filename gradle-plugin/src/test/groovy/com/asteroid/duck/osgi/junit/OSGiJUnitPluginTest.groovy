package com.asteroid.duck.osgi.junit

import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import org.junit.Test

/**
 * A plugin class that adds OSGi JUnit tasks and capabilities to a project
 */
class OSGiJUnitPluginTest {

    @Test
    public void osgiJunitPluginAddsTaskToProject() {
        Project project = ProjectBuilder.builder().build();
        project.pluginManager.apply ''
    }
}
