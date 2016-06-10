package com.martinstephencox.twilightdrift.actors;

import com.martinstephencox.twilightdrift.consts.Consts;
import com.martinstephencox.twilightdrift.interfaces.PlayerInterface;
import com.martinstephencox.twilightdrift.main.ScoreThread;

/**
 * Created by Martin on 31/05/2016.
 */
public class Player implements PlayerInterface {

    //Singleton instance of player object
    private static Player player;

    private int currentPos = Consts.CENTER_POS;
    private int currentTotalScore = 0;  //Total score is only ever increased when the player misses a good target/hits a bad target
    private int currentChunkScore = 0;  //Chunk score is the representation of the players score since their last hit/miss
    private int currentMultiplier = Consts.MIN_SCORE_MULTIPLIER;
    private int lives = Consts.STARTING_LIVES;
    private boolean isInPhaseMode = false;
    private String playerName = "";
    private Thread scoreThread;

    private Player() {
        //Load texture, sprite
    }

    /**
     * Gets the Singleton instance of the player class
     * @return Player The instance of the Player class
     */
    public static Player getPlayer() {
        if (player == null) {
            player = new Player();
        }
        return player;
    }

    /*      GETTERS      */

    public int getCurrentPos() { return currentPos; }

    public int getCurrentTotalScore() { return currentTotalScore; }

    public int getCurrentChunkScore() { return currentChunkScore; }

    public int getCurrentMultiplier() { return currentMultiplier; }

    public String getPlayerName() { return playerName; }

    public int getLives() { return lives; }

    /*    END GETTERS   */

    /*      MODIFY SCORE     */

    public void resetTotalScore() { currentTotalScore = 0; }

    public void resetChunkScore() { currentChunkScore = 0; }

    public void resetMultiplier() { currentMultiplier = Consts.MIN_SCORE_MULTIPLIER; }


    /**
     * Starts updating the chunk score by Consts.UPDATE_SCORE_RATE times per second
     */
    public void startChunkScore(ScoreThread st) {
        int updatePeriod = 1000 / Consts.UPDATE_SCORE_RATE;
        st.setUpdatePeriod(updatePeriod);
        new Thread(st).start();
    }

    /**
     * Updates the chunk score by one
     */
    public void incrementChunkScore() {
        if (currentChunkScore < Consts.MAX_CHUNK_SCORE) {
            currentChunkScore ++;
        }
    }

    /**
     * Updates the score by one
     */
    public void incrementMultiplier() {
        if (currentMultiplier < Consts.MAX_SCORE_MULTIPLIER) {
            currentMultiplier ++;
        }
    }

    /**
     * Player hit a good target, increment the score multiplier and update the chunk score
     * @return int The updated chunk score
     */
    /*public int updateChunkScore() {
        if (currentMultiplier < Consts.MAX_SCORE_MULTIPLIER) {
            currentMultiplier ++;
        }

        if (currentChunkScore + Consts.GOOD_TARGET_VALUE > Consts.MAX_CHUNK_SCORE) {
            currentChunkScore = Consts.MAX_CHUNK_SCORE;
        } else {
            currentChunkScore += Consts.GOOD_TARGET_VALUE;
        }

        return currentChunkScore;
    }*/

    /**
     * Adds the chunk score*multiplier to the total score (will be called when the user misses a good target or hits a bad target)
     * @return int The updated current total score
     */
    public int updateTotalScore() {
        int finalChunkScore = (currentChunkScore * currentMultiplier);

        if (currentTotalScore + finalChunkScore > Consts.MAX_TOTAL_SCORE) {
            currentTotalScore = Consts.MAX_TOTAL_SCORE;
        } else {
            currentTotalScore += finalChunkScore;
        }

        return currentTotalScore;
    }

    /*    END MODIFY SCORE    */


    /*      MODIFY LIVES       */

    public void resetLives() { lives = Consts.STARTING_LIVES; }

    /**
     * Increments the players life counter up to a max of Consts.MAX_LIVES
     * @return int The number of lives the player has
     */
    public int incrementLives() {
        if (lives < Consts.MAX_LIVES) {
            lives++;
        }

        return lives;
    }

    /**
     * Decrements the players life counter
      * @return int The number of lives the player has
     */
    public int decrementLives() {
        lives--;
        return lives;
    }

    /*    END MODIFY LIVES     */

    /*      MODIFY POS/PHASE       */

    /**
     * Moves the player back to the starting/center position
     * @return int The updated player position
     */
    public int resetPos() {
        currentPos = Consts.CENTER_POS;
        return currentPos;
    }

    /**
     * Updates the players position value with respect to left/right bounds
     *
     * @param movement The direction the player is moving
     * @return int The players current position
     */
    public int updatePos(Consts.MOVEMENT movement) {
        switch(movement) {
            case LEFT:
                if (currentPos > Consts.LEFT_BOUND) {
                    currentPos --;
                }
                break;
            case RIGHT:
                if (currentPos < Consts.RIGHT_BOUND) {
                    currentPos ++;
                }
                break;
            case RECENTER:
                currentPos = Consts.CENTER_POS;
                break;
        }

        return currentPos;
    }

    /*    END MODIFY POS/PHASE     */
}
