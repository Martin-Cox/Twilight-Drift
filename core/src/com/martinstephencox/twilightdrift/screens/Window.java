package com.martinstephencox.twilightdrift.screens;

import com.badlogic.gdx.Game;

/**
 * Created by Martin on 06/06/2016.
 */
public class Window extends Game {

    @Override
    public void create() {
        setScreen(new MainMenu());
    }
}
