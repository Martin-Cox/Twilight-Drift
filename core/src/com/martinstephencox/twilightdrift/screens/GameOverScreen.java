package com.martinstephencox.twilightdrift.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.martinstephencox.twilightdrift.actors.Player;
import com.martinstephencox.twilightdrift.consts.Consts;
import com.martinstephencox.twilightdrift.interfaces.PlayerInterface;

/**
 * Created by Martin on 25/06/2016.
 */
public class GameOverScreen implements Screen {

    //Using the PlayerInterface to access Player class
    private PlayerInterface player = Player.getPlayer();

    //The static BG texture
    private Texture staticBgTexture = new Texture(Gdx.files.internal(Consts.IMAGE_STATIC_MENU_BACKGROUND));

    //The scrolling midground texture (the city skyline). Two are needed to provide an "infinite" scroll illusion
    private Texture scrollingMgTextureFirst = new Texture(Gdx.files.internal(Consts.IMAGE_SCROLLING_MENU_MIDGROUND));
    private Texture scrollingMgTextureSecond = new Texture(Gdx.files.internal(Consts.IMAGE_SCROLLING_MENU_MIDGROUND));

    //The scrolling foreground texture (the hills). Two are needed to provide an "infinite" scroll illusion
    private Texture scrollingFgTextureSimple = new Texture(Gdx.files.internal(Consts.IMAGE_SCROLLING_MENU_FOREGROUND_1));
    private Texture scrollingFgTextureDecorated = new Texture(Gdx.files.internal(Consts.IMAGE_SCROLLING_MENU_FOREGROUND_2));

    //Need to keep track of the X position for each texture so we can "move" it by changing the X coordinate
    private int scrollingFgTextureSimpleX = 0;
    private int scrollingFgTextureDecoratedX = Consts.MENU_FOREGROUND_WIDTH;    //Needs to be directly to the right of the first FgTexture
    private int scrollingMgTextureFirstX = 0;
    private int scrollingMgTextureSecondX = Consts.MENU_MIDGROUND_WIDTH;        //Needs to be directly to the right of the first MgTexture

    private SpriteBatch batch;
    private BitmapFont fontEstrogenTitle;
    private BitmapFont fontEstrogenScore;
    private BitmapFont fontEstrogenMenu;

    private float titleR = 0/255f;
    private float titleG = 151/255f;
    private float titleB = 157/255f;

    private float activeItemR = 189/255f;
    private float activeItemG = 13/255f;
    private float activeItemB = 97/255f;

    public GameOverScreen() {
        batch = new SpriteBatch();

        //Load up game over font
        FreeTypeFontGenerator titleGenerator = new FreeTypeFontGenerator(Gdx.files.internal(Consts.FONT_ESTROGEN));
        FreeTypeFontGenerator.FreeTypeFontParameter titleParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        titleParameter.size = 80;
        titleParameter.color = new Color(titleR, titleG, titleB, 1);
        titleParameter.borderWidth = 2;
        titleParameter.borderColor = Color.WHITE;
        titleParameter.shadowColor = Color.WHITE;
        titleParameter.shadowOffsetX = -4;
        titleParameter.shadowOffsetY = 4;
        titleParameter.kerning = true;
        fontEstrogenTitle = titleGenerator.generateFont(titleParameter);
        titleGenerator.dispose();

        //Load up score font
        FreeTypeFontGenerator scoreGenerator = new FreeTypeFontGenerator(Gdx.files.internal(Consts.FONT_ESTROGEN));
        FreeTypeFontGenerator.FreeTypeFontParameter scoreParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        scoreParameter.size = 48;
        scoreParameter.color = new Color(activeItemR, activeItemG, activeItemB, 1);
        scoreParameter.borderWidth = 2;
        scoreParameter.borderColor = Color.WHITE;
        scoreParameter.shadowColor = Color.WHITE;
        scoreParameter.shadowOffsetX = -4;
        scoreParameter.shadowOffsetY = 4;
        scoreParameter.kerning = true;
        fontEstrogenScore = scoreGenerator.generateFont(scoreParameter);
        fontEstrogenMenu = scoreGenerator.generateFont(scoreParameter);
        scoreGenerator.dispose();
    }

    public void show() {

    }

    public void render(float delta) {
        batch.begin();

        //Draw background textures on the screen
        batch.draw(staticBgTexture, 0, 0);
        batch.draw(scrollingMgTextureFirst, scrollingMgTextureFirstX, 0);
        batch.draw(scrollingMgTextureSecond, scrollingMgTextureSecondX, 0);
        batch.draw(scrollingFgTextureSimple, scrollingFgTextureSimpleX, 0);
        batch.draw(scrollingFgTextureDecorated, scrollingFgTextureDecoratedX, 0);

        //All text
        fontEstrogenTitle.draw(batch, "Game Over", 180, 450);
        fontEstrogenScore.draw(batch, "Score: " + getFormattedTotalScore(), 170, 350);
        fontEstrogenMenu.draw(batch, "Press any key", 230, 200);

        batch.end();

        if (Gdx.input.isKeyJustPressed(Input.Keys.ANY_KEY)) {
            player.resetChunkScore();
            player.resetMultiplier();
            player.resetLives();
            player.resetTotalScore();
            MainMenu menuScreen = new MainMenu();
            ((Game) Gdx.app.getApplicationListener()).setScreen(menuScreen);
            this.dispose();
        }

        scrollForeground();
        scrollMidground();
    }

    public void resize(int width, int height) {

    }

    public void pause() {

    }

    public void resume() {

    }

    public void hide() {

    }

    /**
     * Prepends the players total score with zeros
     * @return A formatted string containing the total score
     */
    private String getFormattedTotalScore(){
        String score = "" + player.getCurrentTotalScore();

        return "" + ("0000000000" + score).substring(score.length());
    }

    /**
     * Moves the two foreground textures at a constant rate to the left of the screen. When one texture has moved off the
     * screen enough that it can no longer be seen, it is moved to the right of the screen to provide an "infinite" scroll
     * effect
     */
    private void scrollForeground() {
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
    private void scrollMidground() {
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

    public void dispose() {
        scrollingMgTextureFirst.dispose();
        scrollingMgTextureSecond.dispose();
        scrollingFgTextureSimple.dispose();
        scrollingFgTextureDecorated.dispose();
        fontEstrogenTitle.dispose();
        fontEstrogenScore.dispose();
        fontEstrogenMenu.dispose();
        batch.dispose();
    }
}
