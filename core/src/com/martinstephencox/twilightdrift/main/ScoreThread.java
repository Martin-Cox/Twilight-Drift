package com.martinstephencox.twilightdrift.main;

import com.martinstephencox.twilightdrift.consts.Consts;
import com.martinstephencox.twilightdrift.interfaces.PlayerInterface;

/**
 * Created by Martin on 01/06/2016.
 */
public class ScoreThread implements Runnable {

    /**
     * A separate thread is used to increase the chunk score value every updatePeriod ms to prevent lag/pausing on the main render thread
     */

    private PlayerInterface player;
    private int updatePeriod;
    private volatile boolean isPaused = false;

    public ScoreThread(PlayerInterface p) {
        player = p;
    }

    public void setUpdatePeriod(int up) {
        updatePeriod = up;
    }

    @Override
    public void run() {
        while(true) {
            try {
                if (isPaused) {
                    //The scoring is needs to be disabled for Consts.PAUSE_VALUE because
                    //the player hit a bad object
                    Thread.sleep(Consts.PAUSE_VALUE);
                    isPaused = false;
                } else {
                    //Scoring continues as normal
                    player.incrementChunkScore();
                    Thread.sleep(updatePeriod);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Pauses the score system for Consts.PAUSE_VALUE milliseconds
     */
    public void pauseScoreThread() {
        isPaused = true;
    }

}
