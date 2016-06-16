package com.martinstephencox.twilightdrift.actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;

/**
 * Created by Martin on 02/06/2016.
 */
public abstract class Target {

    private Texture texture;
    private int position;
    private int x;
    private int y;

    public abstract void spawn(Batch batch, int pos);

    public abstract void redraw(Batch batch, int scrollRate);

    public abstract void onHit();

    public abstract void onMiss();

    public abstract int getX();

    public abstract int getY();

    public abstract void despawn();

}
