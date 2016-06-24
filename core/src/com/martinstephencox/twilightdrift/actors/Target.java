package com.martinstephencox.twilightdrift.actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;

/**
 * Created by Martin on 02/06/2016.
 */
public interface Target {

    void spawn(Batch batch);

    void redraw(Batch batch, int scrollRate);

    int getX();

    int getY();

    int getHeight();

    boolean isSpawned();

    void setSpawned();

    void despawn();

}
