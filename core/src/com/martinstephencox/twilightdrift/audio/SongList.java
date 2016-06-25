package com.martinstephencox.twilightdrift.audio;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Martin on 25/06/2016.
 */
public class SongList {

    private ArrayList<Song> songs = new ArrayList<>();

    public SongList() {
        songs.add(new Song(0, "Phoenix #2772", "Rollerblade"));
        songs.add(new Song(1, "BBRAINZ", "Digitales"));
        songs.add(new Song(2, "CVLTVRΣ", "サンセットシティー"));
        songs.add(new Song(3, "CYBEREALITYライフ", "バイスシティ Driving Through the Dawn"));
        songs.add(new Song(4, "ECO VIRTUAL", "Breezy"));
        songs.add(new Song(5, "ECO VIRTUAL", "Clear Skies"));
        songs.add(new Song(6, "Luxury Elite", "Power Surge"));
        songs.add(new Song(7, "Luxury Elite", "Sunkissed"));
        songs.add(new Song(8, "コンシャスTHOUGHTS", "ＳＬＯＷＬＹ"));
        songs.add(new Song(9, "bl00dwave", "４ＬＵＸ"));
        songs.add(new Song(10, "Clinton Affair", "Here And Now"));
        songs.add(new Song(11, "Silver Richards", "Ocean Breeze"));
        songs.add(new Song(12, "Silver Richards", "Sunset"));
    }

    /**
     * Picks a random song that hasn't been played yet
     * @return A song that has not been played
     */
    public Song getRandomSong() {

        checkAllSongsPlayedStatus();

        Random rand = new Random();
        while (true) {
            int trackNumber = rand.nextInt(songs.size());
            //Only play a song which has not been played yet
            if (!songs.get(trackNumber).hasBeenPlayed) {
                songs.get(trackNumber).setHasBeenPlayed();
                return songs.get(trackNumber);
            }
        }

    }

    /**
     * Resets the hasBeenPlayed flag for each song to false
     */
    private void resetSongList() {
        for (Song s : songs) {
            s.resetHasBeenPlayed();
        }
    }

    /**
     * Checks to see if all songs have been played. If all songs have already been played, then
     * reset the hasBeenPlayed flag for each song via resetSongList()
     */
    private void checkAllSongsPlayedStatus() {
        boolean haveAllBeenPlayed = true;

        for (Song s : songs) {
            if (!s.hasBeenPlayed) {
                haveAllBeenPlayed = false;
                break;
            }
        }

        if (haveAllBeenPlayed) {
            resetSongList();
        }
    }

}
