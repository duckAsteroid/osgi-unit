/**
 * Copyright (c) 2016 Dr. Chris Senior.
 * See LICENSE.txt for licensing terms
 */
package com.asteroid.duck.osgi.test.impl;

import com.asteroid.duck.osgi.test.MySimpleService;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

/**
 * Registers our implementation
 */
public class Activator implements BundleActivator {

    private ServiceRegistration<MySimpleService> registration;

    @Override
    public void start(final BundleContext context) throws Exception {
        this.registration = context.registerService(MySimpleService.class, new GreetingServiceImpl(), null);
    }

    @Override
    public void stop(final BundleContext context) throws Exception {
        this.registration.unregister();
        this.registration = null;
    }
}
