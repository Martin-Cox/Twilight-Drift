package com.martinstephencox.twilightdrift.audio;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.martinstephencox.twilightdrift.consts.Consts;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import static java.lang.Math.toIntExact;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Martin on 03/06/2016.
 */
public class BackgroundMusicPlayer {

    private float volume = Consts.MUSIC_VOLUME;
    private Music track;
    private int currentTrackNum = -1;

    public BackgroundMusicPlayer() {}

    public float getVolume() { return volume; }


    /**
     * Try to set the volume for all music
     * @param v The new volume for all music
     */
    public void setVolume(float v) {
        volume = v;
        try {
            track.setVolume(volume);
        } catch (NullPointerException e) {
            //Tried to set volume when no music has been loaded
        }
    }

    /**
     * Start playing background music on loop
     */
    public void startMusic() {
        int numberOfFilesInDirectory = 0;
        int trackNumber;

        //Get number of song files in the music directory
        try {
            numberOfFilesInDirectory = toIntExact(Files.list(Paths.get(Consts.MUSIC_DIRECTORY)).count());
        } catch (IOException e) {

        }

        if (numberOfFilesInDirectory > 1) {

            //Pick a random song from the directory
            Random rand = new Random();
            while (true) {
                trackNumber = rand.nextInt(numberOfFilesInDirectory);
                //Prevent same song playing twice in a row
                if (trackNumber != currentTrackNum) {
                    currentTrackNum = trackNumber;
                    break;
                }
            }

            //Load the track and play
            track = Gdx.audio.newMusic(Gdx.files.internal(Consts.MUSIC_DIRECTORY + "/" + trackNumber + ".mp3"));
            track.setVolume(volume);
            track.play();

            track.setOnCompletionListener(new Music.OnCompletionListener() {
                @Override
                public void onCompletion(Music music) {
                    //On music completion, dispose of old music files from memory and start playing a new track
                    track.dispose();
                    startMusic();
                }
            });
        }
    }

    /**
     * Pause the currently playing track
     */
    public void pause() {
        try {
            track.pause();
        } catch (NullPointerException e) {
            //Tried to pause when no music has been loaded
        }
    }

    /**
     * Resume the currently paused track
     */
    public void resume() {
        try {
            track.play();
        } catch (NullPointerException e) {
            //Tried to play when no music has been loaded
        }
    }

    /**
     * Returns whether a track is currently playing
     * @return True if a music track is playing
     */
    public boolean isPlaying() {
        try {
            return track.isPlaying();
        } catch (NullPointerException e) {
            //Tried to see if music is playing when no music has been loaded
        }
        return true;
    }

    public void lowerVolumeOnHit() {
        try {
            track.setVolume(Consts.HIT_VOLUME_VALUE);
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    track.setVolume(volume);
                }
            }, Consts.PAUSE_VALUE);
        } catch (NullPointerException e) {
            //Tried to set volume when no music has been loaded
        }
    }
}
