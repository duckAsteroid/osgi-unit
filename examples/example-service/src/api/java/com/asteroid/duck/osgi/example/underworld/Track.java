/**
 * Copyright (c) 2016 Dr. Chris Senior.
 * See LICENSE.txt for licensing terms
 */
package com.asteroid.duck.osgi.example.underworld;

/**
 * Information about a track
 */
public class Track {
    private final int id;
    private final String name;
    private final String writers;
    private final String duration;
    private final int albumId;


    public Track(final int id, final String name, final String writers, final String duration, final int albumId) {
        this.id = id;
        this.name = name;
        this.writers = writers;
        this.duration = duration;
        this.albumId = albumId;
    }

    public static Track parse(String line) {
        String[] split = line.split("\t");
        int id = Integer.parseInt(split[0]);
        int albumId = Integer.parseInt(split[4]);
        return new Track(id, split[1], split[2], split[3], albumId);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getWriters() {
        return writers;
    }

    public String getDuration() {
        return duration;
    }

    public int getAlbumId() {
        return albumId;
    }
}
