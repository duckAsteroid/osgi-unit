/**
 * Copyright (c) 2016 Dr. Chris Senior.
 * See LICENSE.txt for licensing terms
 */
package com.asteroid.duck.osgi.junit;

import com.asteroid.duck.osgi.FrameworkHolder;
import com.asteroid.duck.osgi.log.Logger;
import org.junit.rules.ExternalResource;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.launch.Framework;

import static org.junit.Assert.fail;

/**
 * A JUnit rule used to access and control an OSGi Framework
 */
public class WithOSGi extends ExternalResource {
    /** Logging */
    private static final Logger LOG = Logger.getLogger(WithOSGi.class);
    /** the framework */
    private Framework framework = null;

    public WithOSGi() {
    }

    @Override
    protected void before() throws Throwable {
        super.before();
        framework = FrameworkHolder.SINGLETON.getTestFramework();
        if (framework == null) {
            fail("No OSGi framework available");
        }
    }

    /**
     * Access the OSGi framework for this test
     * @return An instance of the OSGi framework
     */
    public Framework getFramework() {
        return framework;
    }


    public Bundle getTestBundle() {
        return FrameworkHolder.getTestBundleFrom(getFramework());
    }

    /**
     * Access the bundle context of the test bundle
     *
     * @return The test bundle context
     */
    public BundleContext getTestBundleContext() {
        return getTestBundle().getBundleContext();
    }

    @Override
    protected void after() {
        framework = null;
        super.after();
    }


}
