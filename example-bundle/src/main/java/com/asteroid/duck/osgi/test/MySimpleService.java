/**
 * Copyright (c) 2016 Dr. Chris Senior.
 * See LICENSE.txt for licensing terms
 */
package com.asteroid.duck.osgi.test;

/**
 * An extremely simple service API for testing
 */
public interface MySimpleService {
    /**
     * Gets a new greeting string from the service
     * @return a greeting for you
     */
    String getGreeting();
}
