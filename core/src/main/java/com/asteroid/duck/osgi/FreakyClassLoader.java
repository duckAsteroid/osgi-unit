package com.asteroid.duck.osgi;

import com.asteroid.duck.osgi.log.Logger;
import org.osgi.framework.Bundle;

import java.util.List;

/**
 * An intercepting class loader - used to load our test classes out of the OSGi framework;
 * I tries to load everything else from the bootstrap classpath
 */
public class FreakyClassLoader extends ClassLoader {
    /** Logging */
    private final Logger LOG = Logger.getLogger(FreakyClassLoader.class);
    /** packages to always load from the parent classpath (and throw CNFE if not found) */
    private final List<String> bootClassPackages;

    public FreakyClassLoader(final ClassLoader parent) {
        super(parent);
        bootClassPackages = Utils.getBootstrapPackages(getPackages());
    }

    /**
     * Simply logs the classes
     */
    @Override
    protected Class<?> loadClass(final String name, final boolean resolve) throws ClassNotFoundException {
        if (LOG.isTraceEnabled()) {
            LOG.trace("loadClass=" + name + ", resolve=" + resolve);
        }
        return super.loadClass(name, resolve);
    }

    /**
     * Called when a class cannot be found by the parent. If it is a known bootstrap package
     * then we throw CNFE otherwise we instantiate OSGi and try to load it from there.
     */
    @Override
    protected Class<?> findClass(final String name) throws ClassNotFoundException {
        String packageName = Utils.getPackageName(name);
        if (LOG.isDebugEnabled()) {
            LOG.debug("findClass name=" + name + " @" + Thread.currentThread().getName());
        }
        if (!Utils.packageMatch(bootClassPackages, packageName)) {
            // ok try to load via OSGi
            if (LOG.isTraceEnabled()) {
                LOG.trace("Find " + name + " in OSGi");
            }
            try {
                Bundle testBundle = FrameworkHolder.SINGLETON.getTestBundle();
                Class<?> osgiClass = testBundle.loadClass(name);

                if (LOG.isDebugEnabled()) {
                    LOG.debug("Loaded " + osgiClass + " from " + testBundle.getSymbolicName() + "@" + testBundle.getLocation());
                }
                return osgiClass;
            } catch (ClassNotFoundException e) {
                throw new ClassNotFoundException(name + " not found by OSGi container test bundle.", e);
            }
        }
        if (LOG.isTraceEnabled()) {
            LOG.trace(name + " is a bootclasspath class");
        }
        throw new ClassNotFoundException(name + " is an bootclasspath class");
    }

    @Override
    public String toString() {
        String s = super.toString();
        s += ":" + getParent().toString();
        return s;
    }
}
