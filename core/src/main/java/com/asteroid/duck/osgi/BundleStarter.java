package com.asteroid.duck.osgi;

import com.asteroid.duck.osgi.log.Logger;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;
import org.osgi.framework.launch.Framework;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.asteroid.duck.osgi.Utils.isFragment;

/**
 * Installs a list of bundles into a framework and starts them
 */
public class BundleStarter {
    /** Logging */
    private static final Logger LOG = Logger.getLogger(BundleStarter.class);
    /** The OSGi framework to install the bundles into */
    private final Framework osgiFramework;
    /** If true then failure to install a bundle is an error. Default false */
    private boolean failOnInstall = false;
    /** If true then failure to start a bundle is an error. Default false */
    private boolean failOnStart = false;

    public BundleStarter(final Framework osgiFramework) {
        if (osgiFramework == null) {
            throw new IllegalArgumentException("Framework cannot be null");
        }
        this.osgiFramework = osgiFramework;
    }

    /**
     * Attempts to install the bundles from the bundle locations
     * @throws IllegalStateException If bundles are already installed
     * @return the bundles that were installed
     */
    public synchronized List<Bundle> installBundles(List<String> bundleLocations) {
        BundleContext ctx = osgiFramework.getBundleContext();
        ArrayList<Bundle> installed = new ArrayList<Bundle>();
        for(String location : bundleLocations) {
            try {
                File bundleFile = new File(location);
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Installing "+location);
                }
                Bundle bundle = ctx.installBundle(bundleFile.toURI().toString());
                installed.add(bundle);
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Installed " + location);
                }
            } catch (BundleException e) {
                LOG.error("Error installing "+location, e);
                if (failOnInstall) {
                    throw new RuntimeException("Unable to install "+location, e);
                }
            }
        }
        return Collections.unmodifiableList(installed);
    }

    /**
     * Start all the installed bundles
     * @throws IllegalStateException If the bundles are not installed
     * @return the bundles that were started
     */
    public synchronized List<Bundle> startBundles(List<Bundle> installed) {
        if (installed == null) {
            throw new IllegalStateException("Bundles not installed");
        }
        ArrayList<Bundle> started = new ArrayList<Bundle>();
        for(Bundle bundle : installed ) {
            try {
                if (!isFragment(bundle)) {
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("Starting " + bundle);
                    }
                    bundle.start();
                }
                started.add(bundle);
            } catch (BundleException e) {
                LOG.error("Error starting "+bundle, e);
                if(failOnStart) {
                    throw new RuntimeException("Unable to start "+bundle, e);
                }
            }
        }
        return Collections.unmodifiableList(started);
    }


}
