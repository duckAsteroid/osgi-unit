/**
 * Copyright (c) 2016 Dr. Chris Senior.
 * See LICENSE.txt for licensing terms
 */
package com.asteroid.duck.osgi.junit;

/**
 * Determines the lifetime (scope) of the OSGi framework made available through the
 * {@link WithOSGi} rule (i.e will the framework be reloaded)
 */
public enum Scope {
    /**
     * The framework instance is shared across all tests in all classes
     */
    STATIC,
    /**
     * The framework instance is shared by all tests in a single class. Each test class
     * get's its own (fresh) framework
     */
    CLASS,
    /**
     * The framework instance is fresh (restarted) for each single test
     */
    METHOD
}
