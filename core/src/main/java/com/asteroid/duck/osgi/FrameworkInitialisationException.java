/**
 * Copyright (c) 2016 Dr. Chris Senior.
 * See LICENSE.txt for licensing terms
 */
package com.asteroid.duck.osgi;

/**
 * An exception throw when the framework cannot be initialised
 */
public class FrameworkInitialisationException extends RuntimeException {

    public FrameworkInitialisationException(final String s) {
        super(s);
    }

    public FrameworkInitialisationException(final Throwable e) {
        super(e);
    }
}
