/**
 * Copyright (c) 2016 Dr. Chris Senior.
 * See LICENSE.txt for licensing terms
 */
package com.asteroid.duck.osgi.junit;

import com.asteroid.duck.osgi.log.Logger;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import static org.junit.Assert.fail;

/**
 * A JUnit rule that provides access to a single instance of a service
 */
public class WithOSGiService<T> extends WithOSGi {
    /** Logging */
    private static final Logger LOG = Logger.getLogger(WithOSGiService.class);
    /** the service class to find an instance of */
    private final Class<T> klazz;
    /** is the service required. i.e. failure to find the service, fails the test */
    private final boolean required;
    /** the instance of the service */
    private T serviceInstance;
    /** OSGi service reference for the service */
    private ServiceReference<T> serviceReference;

    public WithOSGiService(final Class<T> klazz) {
        this(klazz, true);
    }

    public WithOSGiService(final Class<T> klazz, final boolean required) {
        this.klazz = klazz;
        this.required = required;
    }

    @Override
    protected void before() throws Throwable {
        super.before();
        BundleContext ctx = getTestBundleContext();
        if (LOG.isDebugEnabled()) {
            LOG.debug("Locating service: " + klazz.getName());
        }
        serviceReference = ctx.getServiceReference(klazz);
        if (serviceReference != null) {
            if (LOG.isTraceEnabled()) {
                LOG.trace("Getting service instance: " + klazz.getName());
            }
            serviceInstance = ctx.getService(serviceReference);
            if (LOG.isDebugEnabled()) {
                LOG.debug(klazz.getName() + " service instance=" + serviceInstance);
            }
        } else if (required) {
            fail("No service reference found for " + klazz.getName());
        }
    }

    /**
     * Access the service instance that was loaded by this rule from OSGi
     *
     * @return the service instance (if any)
     */
    public T getServiceInstance() {
        return serviceInstance;
    }

    /**
     * Has the service been found in OSGi
     *
     * @return true if found and accessible (may still be null)
     */
    public boolean isFound() {
        return serviceReference != null;
    }

    @Override
    protected void after() {
        if (serviceReference != null) {
            BundleContext ctx = getTestBundleContext();
            if (LOG.isTraceEnabled()) {
                LOG.trace("Unget service instance for " + klazz.getName());
            }
            ctx.ungetService(serviceReference);
            serviceReference = null;
            serviceInstance = null;
        } else {
            LOG.warn("No service reference to dispose for " + klazz.getName());
        }
        super.after();
    }
}
