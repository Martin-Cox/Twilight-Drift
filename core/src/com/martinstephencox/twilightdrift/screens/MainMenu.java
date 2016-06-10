package com.martinstephencox.twilightdrift.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.martinstephencox.twilightdrift.consts.Consts;

/**
 * Created by Martin on 05/06/2016.
 */
public class MainMenu implements Screen {

    //The static BG texture
    private Texture staticBgTexture = new Texture(Gdx.files.internal(Consts.IMAGE_STATIC_MENU_BACKGROUND));

    //The scrolling midground texture (the city skyline). Two are needed to provide an "infinite" scroll illusion
    private Texture scrollingMgTextureFirst = new Texture(Gdx.files.internal(Consts.IMAGE_SCROLLING_MENU_MIDGROUND));
    private Texture scrollingMgTextureSecond = new Texture(Gdx.files.internal(Consts.IMAGE_SCROLLING_MENU_MIDGROUND));

    //The scrolling foreground texture (the hills). Two are needed to provide an "infinite" scroll illusion
    private Texture scrollingFgTextureSimple = new Texture(Gdx.files.internal(Consts.IMAGE_SCROLLING_MENU_FOREGROUND_1));
    private Texture scrollingFgTextureDecorated = new Texture(Gdx.files.internal(Consts.IMAGE_SCROLLING_MENU_FOREGROUND_2));

    private SpriteBatch batch;

    //Need to keep track of the X position for each texture so we can "move" it by changing the X coordinate
    private int scrollingFgTextureSimpleX = 0;
    private int scrollingFgTextureDecoratedX = Consts.MENU_FOREGROUND_WIDTH;    //Needs to be directly to the right of the first FgTexture
    private int scrollingMgTextureFirstX = 0;
    private int scrollingMgTextureSecondX = Consts.MENU_MIDGROUND_WIDTH;        //Needs to be directly to the right of the first MgTexture

    public MainMenu() {
        batch = new SpriteBatch();
    }

    public void show() {

    }

    public void hide() {

    }

    public void render(float delta) {
        batch.begin();

        //Draw each texture on the screen
        batch.draw(staticBgTexture, 0, 0);
        batch.draw(scrollingMgTextureFirst, scrollingMgTextureFirstX, 0);
        batch.draw(scrollingMgTextureSecond, scrollingMgTextureSecondX, 0);
        batch.draw(scrollingFgTextureSimple, scrollingFgTextureSimpleX, 0);
        batch.draw(scrollingFgTextureDecorated, scrollingFgTextureDecoratedX, 0);

        batch.end();

        scrollForeground();
        scrollMidground();

        if (Gdx.input.isKeyPressed(Input.Keys.ANY_KEY)) {
            DebugScreen ds = new DebugScreen();
            ds.create();
            ((Game)Gdx.app.getApplicationListener()).setScreen(ds);
            this.dispose();
        }
    }

    /**
     * Moves the two foreground textures at a constant rate to the left of the screen. When one texture has moved off the
     * screen enough that it can no longer be seen, it is moved to the right of the screen to provide an "infinite" scroll
     * effect
     */
    public void scrollForeground() {
        //Move textures at constant rate to the left by Consts.MENU_FOREGROUND_SCROLL_RATE pixels per frame
        scrollingFgTextureSimpleX -= Consts.MENU_FOREGROUND_SCROLL_RATE;
        scrollingFgTextureDecoratedX -= Consts.MENU_FOREGROUND_SCROLL_RATE;

        //If simple texture goes out of view via the left edge of the window, move it to the right edge of window to give impression of infinite scroll
        if ((scrollingFgTextureSimpleX) < -Consts.MENU_FOREGROUND_WIDTH) {
            scrollingFgTextureSimpleX = Consts.MENU_FOREGROUND_WIDTH - Consts.MENU_FOREGROUND_SCROLL_RATE;
        }

        //If decorated texture goes out of view via the left edge of the window, move it to the right edge of window to give impression of infinite scroll
        if ((scrollingFgTextureDecoratedX) < -Consts.MENU_FOREGROUND_WIDTH) {
            scrollingFgTextureDecoratedX = Consts.MENU_FOREGROUND_WIDTH - Consts.MENU_FOREGROUND_SCROLL_RATE;
        }
    }

    /**
     * Moves the two midground textures at a constant rate to the left of the screen. When one texture has moved off the
     * screen enough that it can no longer be seen, it is moved to the right of the screen to provide an "infinite" scroll
     * effect. Although only one distinct midground texture exists, two of them need to be drawn in order to provide the
     * "infinite" scroll effect (If we only had one, then there would be a 800 pixel gap between the end of the texture moving
     * off the left edge of the screen and the beginning of the texture appearing on the right of the screen).
     */
    public void scrollMidground() {
        //Move textures at constant rate to the left by Consts.MENU_MIDGROUND_SCROLL_RATE pixels per frame
        scrollingMgTextureFirstX -= Consts.MENU_MIDGROUND_SCROLL_RATE;
        scrollingMgTextureSecondX -= Consts.MENU_MIDGROUND_SCROLL_RATE;

        //If first texture goes out of view via the left edge of the window, move it to the right edge of window to give impression of infinite scroll
        if ((scrollingMgTextureFirstX) < -Consts.MENU_MIDGROUND_WIDTH) {
            scrollingMgTextureFirstX = Consts.MENU_MIDGROUND_WIDTH - Consts.MENU_MIDGROUND_SCROLL_RATE;
        }

        //If second texture goes out of view via the left edge of the window, move it to the right edge of window to give impression of infinite scroll
        if ((scrollingMgTextureSecondX) < -Consts.MENU_MIDGROUND_WIDTH) {
            scrollingMgTextureSecondX = Consts.MENU_MIDGROUND_WIDTH - Consts.MENU_MIDGROUND_SCROLL_RATE;
        }
    }

    public void resize(int width, int height) {

    }

    public void pause() {

    }

    public void resume() {

    }

    public void dispose() {
        scrollingMgTextureFirst.dispose();
        scrollingMgTextureSecond.dispose();
        scrollingFgTextureSimple.dispose();
        scrollingFgTextureDecorated.dispose();
        batch.dispose();
    }
}
