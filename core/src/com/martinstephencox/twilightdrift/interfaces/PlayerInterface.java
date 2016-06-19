package com.martinstephencox.twilightdrift.interfaces;

import com.badlogic.gdx.graphics.Texture;
import com.martinstephencox.twilightdrift.actors.Player;
import com.martinstephencox.twilightdrift.consts.Consts;
import com.martinstephencox.twilightdrift.main.ScoreThread;

/**
 * Created by Martin on 08/06/2016.
 */
public interface PlayerInterface {

    int getCurrentPos();

    int getCurrentTotalScore();

    int getCurrentChunkScore();

    int getCurrentMultiplier();

    String getPlayerName();

    int getLives();

    void resetTotalScore();

    void resetChunkScore();

    void resetMultiplier();

    Texture getTexture();

    int getY();

    int getX();

    int getAdjustedX();

    void startChunkScore(ScoreThread st);

    void incrementChunkScore();

    void incrementMultiplier();

    int updateChunkScore();

    int updateTotalScore();

    void resetLives();

    int incrementLives();

    int decrementLives();

    int updatePos(Consts.MOVEMENT movement);
}
