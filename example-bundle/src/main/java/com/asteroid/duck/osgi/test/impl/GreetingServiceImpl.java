/**
 * Copyright (c) 2016 Dr. Chris Senior.
 * See LICENSE.txt for licensing terms
 */
package com.asteroid.duck.osgi.test.impl;

import com.asteroid.duck.osgi.test.MySimpleService;

/**
 * A really simple implementation
 */
public class GreetingServiceImpl implements MySimpleService {
    @Override
    public String getGreeting() {
        return "There you are, your own number on your very own door." +
                "And behind that door, your very own office! "+
                "Welcome to the team, DZ-015";
    }
}
