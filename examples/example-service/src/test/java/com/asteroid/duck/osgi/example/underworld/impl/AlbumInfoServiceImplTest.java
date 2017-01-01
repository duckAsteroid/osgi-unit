package com.asteroid.duck.osgi.example.underworld.impl;

import com.asteroid.duck.osgi.example.underworld.Album;
import com.asteroid.duck.osgi.example.underworld.Track;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

/**
 *
 */
public class AlbumInfoServiceImplTest {
    private AlbumInfoServiceImpl subject = new AlbumInfoServiceImpl();
    @Test
    public void testAlbums() {
        List<Album> albums = subject.getAlbums();
        assertNotNull(albums);
        assertEquals(2, albums.size());
    }

    @Test
    public void testTracks() {
        List<Track> tracks = subject.getTracks();
        assertNotNull(tracks);
        assertEquals(17, tracks.size());
    }
}