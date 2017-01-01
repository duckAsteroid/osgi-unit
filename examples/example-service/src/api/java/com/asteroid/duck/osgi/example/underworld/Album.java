/**
 * Copyright (c) 2016 Dr. Chris Senior.
 * See LICENSE.txt for licensing terms
 */
package com.asteroid.duck.osgi.example.underworld;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Information about a studio album
 */
public class Album {
    private final int id;
    private final String name;
    private final Date releaseDate;
    private final String label;

    public static final DateFormat DATE_FORMAT = new SimpleDateFormat("dd/mm/yyyy");

    public Album(final int id, final String name, final Date releaseDate, final String label) {
        this.id = id;
        this.name = name;
        this.releaseDate = releaseDate;
        this.label = label;
    }

    public static Album parse(String line) {
        String[] split = line.split("\t");
        int id = Integer.parseInt(split[0]);
        Date releaseDate = null;
        try {
            releaseDate = DATE_FORMAT.parse(split[2]);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new Album(id, split[1], releaseDate, split[3]);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public String getLabel() {
        return label;
    }

    @Override
    public String toString() {
        return id +"\t" + name + "\t" + DATE_FORMAT.format(releaseDate) + "\t" + label;
    }
}
