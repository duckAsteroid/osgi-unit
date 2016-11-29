package com.asteroid.duck.example.test;

//import com.asteroid.duck.osgi.junit.WithOSGi;
import com.asteroid.duck.example.MySimpleService;
import com.asteroid.duck.osgi.junit.WithOSGi;
import org.junit.Rule;
import org.junit.Test;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import static org.junit.Assert.*;

/**
 * An example of how to use OSGi in tests
 */
public class MySimpleServiceTest {

    @Rule
    public WithOSGi osgi = new WithOSGi();

    @Test
    public void getGreeting() throws Exception {
        BundleContext ctx = osgi.getTestBundleContext();
        assertNotNull(ctx);
        ServiceReference<MySimpleService> sr = ctx.getServiceReference(MySimpleService.class);
        assertNotNull(sr);
        MySimpleService simpleService = ctx.getService(sr);
        assertNotNull(simpleService);
        String greeting = simpleService.getGreeting();
        assertNotNull(greeting);
        assertTrue(greeting.length() > 0);
    }

}