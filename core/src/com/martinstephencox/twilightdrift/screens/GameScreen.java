package com.martinstephencox.twilightdrift.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.martinstephencox.twilightdrift.consts.Consts;

/**
 * Created by Martin on 05/06/2016.
 */
public class GameScreen implements Screen {

    private Texture waveTexture = new Texture(Gdx.files.internal(Consts.IMAGE_GAME_WAVES));

    private SpriteBatch batch;

    public GameScreen() {
        batch = new SpriteBatch();
    }


    public void show() {

    }

    public void render(float delta) {

        Gdx.gl.glClearColor(252/255f, 209/255f, 230/255f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        batch.draw(waveTexture, 0, 0);
        batch.end();

    }

    public void resize(int width, int height) {

    }

    public void pause() {

    }

    public void resume() {

    }

    public void hide() {

    }

    public void dispose() {

    }

    /*public void scrollWaves() {
        //Move textures down at scrollRate pixels per frame
        wavesFirstY -= scrollRate;
        wavesSecondY -= scrollRate;

        //If first texture goes out of view via the bottom edge of the window, move it to the top edge of window to give impression of infinite scroll
        if ((wavesFirstY) < -600) {
            wavesFirstY = 600 - scrollRate;
        }

        //If second texture goes out of view via the bottom edge of the window, move it to the top edge of window to give impression of infinite scroll
        if ((wavesSecondY) < -600) {
            wavesSecondY = 600 - scrollRate;
        }
    }*/
}
