package com.asteroid.duck.example.test;

//import com.asteroid.duck.osgi.junit.WithOSGi;
import com.asteroid.duck.example.MySimpleService;
import com.asteroid.duck.osgi.junit.WithOSGi;
import com.asteroid.duck.osgi.junit.WithOSGiService;
import org.junit.Rule;
import org.junit.Test;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.launch.Framework;

import static org.junit.Assert.*;

/**
 * An example of how to use OSGi in tests
 */
public class MySimpleServiceTest {

    @Rule
    public WithOSGi osgi = new WithOSGi();

    @Rule
    public WithOSGiService<MySimpleService> service = new WithOSGiService<MySimpleService>(MySimpleService.class);

    @Test
    public void testOsgiRule() throws Exception {
        // validate framework
        {
            Framework framework = osgi.getFramework();
            assertNotNull(framework);
            assertTrue(framework.getState() == Framework.ACTIVE);
        }
        //test bundle
        {
            Bundle testBundle = osgi.getTestBundle();
            assertNotNull(testBundle);
            assertTrue(testBundle.getState() == Framework.ACTIVE);
        }
        // test bundle context
        {
            BundleContext ctx = osgi.getTestBundleContext();
            assertNotNull(ctx);
            ServiceReference<MySimpleService> sr = ctx.getServiceReference(MySimpleService.class);
            assertNotNull(sr);
            MySimpleService simpleService = ctx.getService(sr);
            checkService(simpleService);
        }
    }

    @Test
    public void testOsgiServiceRule() throws Exception {
        MySimpleService serviceInstance = service.getServiceInstance();
        checkService(serviceInstance);
    }

    private static void checkService(MySimpleService instance) {
        assertNotNull(instance);
        String greeting = instance.getGreeting();
        assertNotNull(greeting);
        assertTrue(greeting.length() > 0);
    }

}