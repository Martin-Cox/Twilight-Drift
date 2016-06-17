package com.martinstephencox.twilightdrift.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.martinstephencox.twilightdrift.consts.Consts;

/**
 * Created by Martin on 16/06/2016.
 */
public class BadTarget implements Target {

    private Texture texture;
    private int y = Consts.SPAWN_Y;
    private int adjustedX;
    private int x;
    private boolean isSpawned = false;

    public BadTarget(int pos) {
        super();
        switch (pos) {
            case 0:
                x = Consts.LEFT_POS;
                break;
            case 1:
                x = Consts.MID_LEFT_POS;
                break;
            case 2:
                x = Consts.CENTER_POS;
                break;
            case 3:
                x = Consts.MID_RIGHT_POS;
                break;
            case 4:
                x = Consts.RIGHT_POS;
                break;
        }
    }

    /**
     * Creates a new sprite on the game screen using the position values already defined at instance creation
     * @param batch The batch object to draw the sprite onto
     */
    public void spawn(Batch batch) {
        batch.begin();
        texture = new Texture(Gdx.files.internal(Consts.IMAGE_BAD_TARGET));
        adjustedX = x - texture.getWidth()/2;
        setSpawned();
        batch.draw(texture, x, Consts.SPAWN_Y);
        batch.end();
    }

    /**
     * Redraws the instance of a target by moving it vertically down at scrollRate pixels per frame
     * @param batch The batch object to draw the sprite onto
     * @param scrollRate The number of pixels to move the target vertically down by per frame
     */
    public void redraw(Batch batch, int scrollRate) {
        y -= scrollRate;

        //Remove sprite if it goes off the screen
        if (y < 0 - texture.getHeight()) {
            despawn();
        } else {
            batch.begin();
            batch.draw(texture, adjustedX, y);
            batch.end();
        }
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getHeight() { return texture.getHeight(); }

    public void onHit() {

    }

    public void onMiss() {

    }

    public boolean isSpawned() { return isSpawned; }

    public void setSpawned() { isSpawned = true; }

    public void despawn() {
        texture.dispose();
    }
}
