package com.asteroid.duck.osgi.test;

import com.asteroid.duck.osgi.junit.WithOSGi;
import org.junit.Rule;
import org.junit.Test;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.launch.Framework;

import static org.junit.Assert.*;

/**
 *
 */
public class MySimpleServiceTest {
    @Rule
    public WithOSGi osgi = new WithOSGi();

    @Test
    public void getGreeting() throws Exception {
        Framework fw = osgi.getFramework();
        assertNotNull(fw);
        BundleContext ctx = fw.getBundleContext();
        assertNotNull(ctx);
        ServiceReference<MySimpleService> sr = ctx.getServiceReference(MySimpleService.class);
        assertNotNull(sr);
        MySimpleService simpleService = ctx.getService(sr);
        assertNotNull(simpleService);
    }

}