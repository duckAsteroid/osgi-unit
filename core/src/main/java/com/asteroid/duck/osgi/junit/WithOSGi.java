/**
 * Copyright (c) 2016 Dr. Chris Senior.
 * See LICENSE.txt for licensing terms
 */
package com.asteroid.duck.osgi.junit;

import org.junit.rules.ExternalResource;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.osgi.framework.launch.Framework;

/**
 * A JUnit rule used to access and control an OSGi Framework
 */
public class WithOSGi extends ExternalResource {
    Framework framework;

    public WithOSGi() {

    }

    @Override
    protected void before() throws Throwable {
        FrameworkBuilder builder = new FrameworkBuilder();
        framework = builder.build();
        framework.start();
    }

    /**
     * Access the OSGi framework for this test
     * @return An instance of the OSGi framework
     */
    public Framework getFramework() {
        return framework;
    }

    @Override
    protected void after() {
        super.after();
    }

    /**
     * A statement to extract configuration
     */
    public class ConfigurableStatement extends Statement {

        private final Statement base;
        private final Description description;

        public ConfigurableStatement(final Statement base, final Description description) {
            this.base = base;
            this.description = description;
        }

        @Override
        public void evaluate() throws Throwable {
            // get configuration data from the description

            base.evaluate();
        }
    }
}
