package com.asteroid.duck.osgi.junit;

import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;

/**
 * Knows to start an OSGi framework and execute the test inside it
 */
public class OSGiFramework extends BlockJUnit4ClassRunner {
    /**
     * Creates a BlockJUnit4ClassRunner to run {@code klass}
     *
     * @param klass
     *
     * @throws InitializationError if the test class is malformed.
     */
    public OSGiFramework(final Class<?> klass) throws InitializationError {
        super(klass);
    }
}
