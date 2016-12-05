/**
 * Copyright (c) 2016 Dr. Chris Senior.
 * See LICENSE.txt for licensing terms
 */
package com.asteroid.duck.osgi.junit;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

/**
 * A JUnit rule that provides access to a single instance of a service
 */
public class WithOSGiService<T> extends WithOSGi {
    /** the service class to find an instance of */
    private final Class<T> klazz;
    /** the instance of the service */
    private T serviceInstance;
    /** OSGi service reference for the service */
    private ServiceReference<T> serviceReference;

    public WithOSGiService(final Class<T> klazz) {
        this.klazz = klazz;
    }

    @Override
    protected void before() throws Throwable {
        super.before();
        BundleContext ctx = getTestBundleContext();
        serviceReference = ctx.getServiceReference(klazz);
        if (serviceReference != null) {
            serviceInstance = ctx.getService(serviceReference);
        }
    }

    @Override
    protected void after() {
        if (serviceReference != null) {
            BundleContext ctx = getTestBundleContext();
            ctx.ungetService(serviceReference);
            serviceReference = null;
            serviceInstance = null;
        }
        super.after();
    }
}
