/**
 * Copyright (c) 2016 Dr. Chris Senior.
 * See LICENSE.txt for licensing terms
 */
package junit;

import com.asteroid.duck.osgi.junit.WithOSGi;
import org.junit.Rule;
import org.junit.Test;
import org.osgi.framework.launch.Framework;
import org.osgi.framework.launch.FrameworkFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import static org.junit.Assert.assertNotNull;


public class WithOSGiTest {

    @Rule
    public WithOSGi osgi = new WithOSGi();

    @Test
    public void testRule() {
        assertNotNull(osgi);
        Framework framework = osgi.getFramework();
        assertNotNull(framework);
    }
}
