package com.martinstephencox.twilightdrift.audio;

import com.martinstephencox.twilightdrift.consts.Consts;

/**
 * Created by Martin on 04/06/2016.
 */
public class Song {

    private int trackNumber;
    private String artist;
    private String trackName;
    private String filename;
    boolean hasBeenPlayed = false;

    public Song(int trackNumber, String artist, String trackName) {
        this.trackNumber = trackNumber;
        this.artist = artist;
        this.trackName = trackName;
        filename = Consts.MUSIC_DIRECTORY + trackNumber + ".mp3";
    }

    public String getFilename() { return filename; }

    public String getArtist() { return artist; }

    public String getTrackName() { return trackName; }

    public boolean hasBeenPlayed () { return hasBeenPlayed; }

    public void setHasBeenPlayed() { hasBeenPlayed = true; }

    public void resetHasBeenPlayed() { hasBeenPlayed = false; }

}
