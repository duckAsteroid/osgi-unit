/**
 * Copyright (c) 2017 Dr. Chris Senior
 */
package com.asteroid.duck.example.impl;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

/**
 * A plain old JUnit testing the IMPL without OSGi
 */
public class GreetingServiceImplTest {

    @Test
    public void testGreeting() {
        GreetingServiceImpl subject = new GreetingServiceImpl();
        String greeting = subject.getGreeting();
        System.out.println("greeting=" + greeting);
        assertNotNull(greeting);
        assertEquals(124, greeting.length());
        assertThat(greeting, allOf(startsWith("There you are"), endsWith("DZ-015")));
    }
}
