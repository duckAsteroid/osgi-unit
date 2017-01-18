/**
 * Copyright (c) 2017 Dr. Chris Senior
 */
package com.asteroid.duck.example.test;

import com.asteroid.duck.example.impl.GreetingServiceImpl;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

/**
 * A plain old JUnit testing the IMPL without OSGi
 */
public class MySimpleServiceImplTest {

    @Test
    public void testGreeting() {
        GreetingServiceImpl subject = new GreetingServiceImpl();
        String greeting = subject.getGreeting();
        assertNotNull(greeting);
        assertEquals(124, greeting.length());
        assertThat(greeting, allOf(startsWith("There you are"), endsWith("DZ-015")));
    }
}
