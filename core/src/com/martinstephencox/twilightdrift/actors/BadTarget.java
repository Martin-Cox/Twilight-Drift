package com.martinstephencox.twilightdrift.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.martinstephencox.twilightdrift.consts.Consts;

/**
 * Created by Martin on 16/06/2016.
 */
public class BadTarget extends Target {

    Texture texture = new Texture(Gdx.files.internal(Consts.IMAGE_BAD_TARGET));
    int y = Consts.SPAWN_Y;
    int adjustedX;
    int x;

    public BadTarget() {
        super();
    }

    public void spawn(Batch batch, int pos) {
        batch.begin();
        switch (pos) {
            case 0:
                x = Consts.LEFT_POS;
                adjustedX = x - texture.getWidth()/2;
                break;
            case 1:
                x = Consts.MID_LEFT_POS;
                adjustedX = x - texture.getWidth()/2;
                break;
            case 2:
                x = Consts.CENTER_POS;
                adjustedX = x - texture.getWidth()/2;
                break;
            case 3:
                x = Consts.MID_RIGHT_POS;
                adjustedX = x - texture.getWidth()/2;
                break;
            case 4:
                x = Consts.RIGHT_POS;
                adjustedX = x - texture.getWidth()/2;
                break;
        }
        batch.draw(texture, x, Consts.SPAWN_Y);
        batch.end();
    }

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

    public void onHit() {

    }

    public void onMiss() {

    }

    public void despawn() {
        texture.dispose();
    }
}
