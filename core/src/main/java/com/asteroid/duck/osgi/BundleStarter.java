package com.asteroid.duck.osgi;

import org.osgi.framework.*;
import org.osgi.framework.launch.Framework;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Installs a list of bundles into a framework and starts them
 */
public class BundleStarter {
    /** Logging */
    private static final Logger LOG = LoggerFactory.getLogger(BundleStarter.class);
    /** The OSGi framework to install the bundles into */
    private final Framework osgiFramework;
    /** A list of bundles to install */
    private List<String> bundleLocations = new ArrayList<String>();
    /** If true then failure to install a bundle is an error. Default false */
    private boolean failOnInstall = false;
    /** If true then failure to start a bundle is an error. Default false */
    private boolean failOnStart = false;
    /** A list of the installed bundles */
    private List<Bundle> installed = null;
    /** A list of the started bundles */
    private List<Bundle> started = null;

    public BundleStarter(final Framework osgiFramework) {
        if (osgiFramework == null) {
            throw new IllegalArgumentException("Framework cannot be null");
        }
        this.osgiFramework = osgiFramework;
    }

    /**
     * Attempts to install the bundles from the bundle locations
     * @throws IllegalStateException If bundles are already installed
     */
    public synchronized void installBundles() {
        if (installed != null) {
            throw new IllegalStateException("Bundles already installed");
        }
        // start installing bundles
        BundleContext ctx = osgiFramework.getBundleContext();
        installed = new ArrayList<Bundle>();
        for(String location : bundleLocations) {
            try {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Installing "+location);
                }
                Bundle bundle = ctx.installBundle(location);
                installed.add(bundle);
            } catch (BundleException e) {
                LOG.error("Error installing "+location, e);
                if (failOnInstall) {
                    throw new RuntimeException("Unable to install "+location, e);
                }
            }
        }
    }

    /**
     * Start all the installed bundles
     * @throws IllegalStateException If the bundles are not installed
     */
    public synchronized void startBundles() {
        if (installed == null) {
            throw new IllegalStateException("Bundles not installed");
        }
        started = new ArrayList<Bundle>();
        for(Bundle bundle : installed ) {
            try {
                bundle.start();
                started.add(bundle);
            } catch (BundleException e) {
                LOG.error("Error starting "+bundle, e);
                if(failOnStart) {
                    throw new RuntimeException("Unable to start "+bundle, e);
                }
            }
        }
    }
}
