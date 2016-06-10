package com.martinstephencox.twilightdrift.actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

/**
 * Created by Martin on 02/06/2016.
 */
public abstract class Target {

    private Sprite sprite;
    private Texture texture;
    private int position;
    private double x;
    private double y;


    public abstract void onHit();

    public abstract void onMiss();

}
