/**
 * Copyright (c) 2016 Dr. Chris Senior.
 * See LICENSE.txt for licensing terms
 */
package com.asteroid.duck.osgi.example.underworld.impl;

import com.asteroid.duck.osgi.example.underworld.Album;
import com.asteroid.duck.osgi.example.underworld.AlbumInfoService;
import com.asteroid.duck.osgi.example.underworld.Track;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;

/**
 * A simple implementation of the album info service (uses a CSV file)
 */
public class AlbumInfoServiceImpl implements AlbumInfoService {
    private List<Album> albums = loadData("/album-data.csv", new DataFactory<Album>() {
        @Override
        public Album parse(final String line) {
            return Album.parse(line);
        }
    });

    private List<Track> tracks = loadData("/track-data.csv", new DataFactory<Track>() {
        @Override
        public Track parse(final String line) {
            return Track.parse(line);
        }
    });

    private interface DataFactory<T> {
        T parse(String line);
    }

    private <T> List<T> loadData(String resource, DataFactory<T> factory) {
        List<T> results = new ArrayList<>();
        InputStream stream = AlbumInfoService.class.getResourceAsStream(resource);
        InputStreamReader reader = new InputStreamReader(stream);
        LineIterator lines = new LineIterator(reader);
        while(lines.hasNext()) {
            String line = lines.nextLine();
            if (line.startsWith("#") || line.length() == 0) {
                continue;
            }
            T data = factory.parse(line);
            results.add(data);
        }
        return Collections.unmodifiableList(results);
    }

    public List<Album> getAlbums() {
        return albums;
    }

    public List<Track> getTracks() {
        return tracks;
    }
}
