/**
 * Copyright (c) 2016 Dr. Chris Senior.
 * See LICENSE.txt for licensing terms
 */
package com.asteroid.duck.osgi;

import com.asteroid.duck.osgi.log.Logger;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleException;
import org.osgi.framework.launch.Framework;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;

/**
 * Holds the singleton instance of the OSGi Framework.
 * Starts the framework if required.
 */
public class FrameworkHolder {
    private final Logger LOG = Logger.getLogger(toString() + getClass().getClassLoader().toString());

    public static final FrameworkHolder SINGLETON = new FrameworkHolder();

    /**
     * Use {@link #SINGLETON}
     */
    private FrameworkHolder() {
    }

    private Framework framework = null;

    public final Framework getTestFramework() throws FrameworkInitialisationException {
        synchronized (SINGLETON) {
            if (framework == null) {
                initialiseFramework();
            }
            return framework;
        }
    }

    private void initialiseFramework() {
        synchronized (SINGLETON) {
            FrameworkBuilder builder = new FrameworkBuilder();
            try {
                // use the system properties of this class
                builder.withConfigProperties(System.getProperties());
                // create a framework instance
                framework = builder.build();
                LOG.debug("New framework instance created: " + framework);
                // these are the bundle locations...
                LinkedList<String> locations = new LinkedList<String>();
                // the name of a file that tells us what bundles to install/start
                String bundles = getRequiredProperty(Constants.RUNTIME_BUNDLES);
                if (bundles.startsWith(Constants.PATH)) {
                    readBundlePath(bundles, locations);
                } else {
                    readBundleFile(bundles, locations);
                }

                // start the framework
                framework.start();
                // helper to install and start the bundles
                BundleStarter starter = new BundleStarter(framework);
                LOG.trace("Installing " + locations.size() + " bundles");
                // install them
                List<Bundle> installedBundles = starter.installBundles(locations);
                LOG.info("Installed " + installedBundles.size() + " bundles");

                // start them
                List<Bundle> startedBundles = starter.startBundles(installedBundles);
                LOG.info("Started " + installedBundles.size() + " bundles");
            } catch (IOException e) {
                throw new FrameworkInitialisationException(e);
            } catch (BundleException e) {
                throw new FrameworkInitialisationException(e);
            }
        }
    }

    /** Parse a classpath style path string into a list of bundles */
    private void readBundlePath(final String bundles, final LinkedList<String> locations) {
        String path = bundles.substring(Constants.PATH.length());
        String[] split = path.split(";");
        for (String element : split) {
            locations.add(element);
        }
    }

    /** Read a file (line per entry) into a list of bundles */
    private void readBundleFile(final String bundles, final LinkedList<String> locations) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(bundles)));
        String line;
        while ((line = reader.readLine()) != null) {
            locations.add(line);
        }
    }

    private static String getRequiredProperty(String name) throws FrameworkInitialisationException {
        String value = System.getProperty(name);
        if (value == null) {
            throw new FrameworkInitialisationException("Required property '" + name + "' not set");
        }
        return value;
    }

    public Bundle getTestBundle() throws FrameworkInitialisationException {
        return getTestBundleFrom(getTestFramework());
    }

    public static Bundle getTestBundleFrom(Framework fw) throws FrameworkInitialisationException {
        String testBundleId = getRequiredProperty(Constants.TEST_BUNDLE);
        Bundle bundle = Utils.getBundle(fw.getBundleContext(), testBundleId, true);
        if (bundle == null) {
            throw new FrameworkInitialisationException("No " + Constants.TEST_BUNDLE + " with symbolicName=" + testBundleId);
        }
        return bundle;
    }
}
