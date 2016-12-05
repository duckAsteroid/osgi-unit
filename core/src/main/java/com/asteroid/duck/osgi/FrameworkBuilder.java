/**
 * Copyright (c) 2016 Dr. Chris Senior.
 * See LICENSE.txt for licensing terms
 */
package com.asteroid.duck.osgi;

import org.osgi.framework.launch.Framework;
import org.osgi.framework.launch.FrameworkFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * A builder for creating and initialising instances of an OSGi {@link Framework}
 */
public class FrameworkBuilder {

    /** Configuration properties for the framework */
    private Map<String, String> configuration = new HashMap<>();

    /** replaces the configuration with the given one */
    public FrameworkBuilder withConfig(Map<String, String> cfg) {
        this.configuration = cfg;
        return this;
    }

    /** Adds a single property to the configuration */
    public FrameworkBuilder withProperty(String key, String value) {
        if (key != null) {
            if (value == null) {
                value = "";
            }
            this.configuration.put(key, value);
        }
        return this;
    }

    /** adds all the given properties to the configuration */
    public FrameworkBuilder withConfigProperties(Properties props) {
        for (Map.Entry<Object, Object> entry : props.entrySet()) {
            Object key = entry.getKey();
            Object value = entry.getValue();
            if (key != null) {
                withProperty(key.toString(), value != null ? value.toString() : null);
            }
        }
        return this;
    }

    /** Reads the properties from a file then uses them */
    public FrameworkBuilder withConfigFile(InputStream stream) throws IOException {
        Properties props = new Properties();
        props.load(stream);
        return withConfigProperties(props);
    }

    /** Create an instance of framework */
    public Framework build() {
        ServiceLoader<FrameworkFactory> loader = ServiceLoader.load(FrameworkFactory.class);
        Iterator<FrameworkFactory> factoryIterator = loader.iterator();
        while (factoryIterator.hasNext()) {
            FrameworkFactory factory = factoryIterator.next();
            if (factory != null) {
                return factory.newFramework(configuration);
            }
        }
        throw new NoSuchElementException("Unable to load framework");
    }
}
