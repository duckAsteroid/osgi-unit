/**
 * Copyright (c) 2016 Dr. Chris Senior.
 * See LICENSE.txt for licensing terms
 */
package com.asteroid.duck.osgi;

/**
 * Various constants used by the plugin and the core
 */
public interface Constants {
    /** Group name for this JAR (and the gradle plugin) */
    String GROUP = "com.asteroid.duck";
    /** Artifact ID for this JAR */
    String ARTIFACT = "osgi-unit-core";
    /** Version number of this and the plugin */
    String VERSION = "1.0.+";
    /** Gradle (maven) co-ordinates of the core artifact */
    String CORE_ARTIFACT_COORD = GROUP + ":" + ARTIFACT + ":" + VERSION;
    /** The name of our core jar file */
    String JAR_NAME = ARTIFACT + "-" + VERSION + ".jar";

    /** System property that indicates the file that list the bundles to install or contains a path style (; separated) list of them */
    String RUNTIME_BUNDLES = "runtime.bundles";
    /** System property that indicates the test bundles symbolic name */
    String TEST_BUNDLE = "test.bundle";
    /** System property to specify/override system package names */
    String SYSTEM_PACKAGES = "com.asteroid.duck.osgi.system.packages";
    /** Prefix for runtime.bundles property to indicate the value is a path, rather than a filename */
    String PATH = "path=";
}
