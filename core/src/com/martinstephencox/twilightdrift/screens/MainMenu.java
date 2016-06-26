package com.martinstephencox.twilightdrift.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
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

    //Need to keep track of the X position for each texture so we can "move" it by changing the X coordinate
    private int scrollingFgTextureSimpleX = 0;
    private int scrollingFgTextureDecoratedX = Consts.MENU_FOREGROUND_WIDTH;    //Needs to be directly to the right of the first FgTexture
    private int scrollingMgTextureFirstX = 0;
    private int scrollingMgTextureSecondX = Consts.MENU_MIDGROUND_WIDTH;        //Needs to be directly to the right of the first MgTexture

    private SpriteBatch batch;
    private BitmapFont fontEstrogenTitle;
    private BitmapFont fontEstrogenMenuPlay;
    private BitmapFont fontEstrogenMenuHelp;
    private BitmapFont fontEstrogenMenuExit;
    private BitmapFont fontEstrogenMenuPlayActive;
    private BitmapFont fontEstrogenMenuHelpActive;
    private BitmapFont fontEstrogenMenuExitActive;

    private float titleR = 0/255f;
    private float titleG = 151/255f;
    private float titleB = 157/255f;

    private float itemR = 0/255f;
    private float itemG = 151/255f;
    private float itemB = 157/255f;

    private float activeItemR = 189/255f;
    private float activeItemG = 13/255f;
    private float activeItemB = 97/255f;

    private enum currentOptionValues {PLAY, HELP, EXIT}

    private currentOptionValues currentOption = currentOptionValues.PLAY;

    private boolean showHelp = false;

    public MainMenu() {
        batch = new SpriteBatch();

        //Load up title font
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

        //Load up normal menu item font
        FreeTypeFontGenerator menuItemGenerator = new FreeTypeFontGenerator(Gdx.files.internal(Consts.FONT_ESTROGEN));
        FreeTypeFontGenerator.FreeTypeFontParameter menuItemParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        menuItemParameter.size = 48;
        menuItemParameter.color = new Color(itemR, itemG, itemB, 1);
        menuItemParameter.borderWidth = 2;
        menuItemParameter.borderColor = Color.WHITE;
        menuItemParameter.shadowColor = Color.WHITE;
        menuItemParameter.shadowOffsetX = -4;
        menuItemParameter.shadowOffsetY = 4;
        menuItemParameter.kerning = true;
        fontEstrogenMenuPlay = menuItemGenerator.generateFont(menuItemParameter);
        fontEstrogenMenuHelp = menuItemGenerator.generateFont(menuItemParameter);
        fontEstrogenMenuExit = menuItemGenerator.generateFont(menuItemParameter);
        menuItemGenerator.dispose();

        //Load up active menu item font
        FreeTypeFontGenerator menuItemActiveGenerator = new FreeTypeFontGenerator(Gdx.files.internal(Consts.FONT_ESTROGEN));
        FreeTypeFontGenerator.FreeTypeFontParameter menuItemActiveParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        menuItemActiveParameter.size = 48;
        menuItemActiveParameter.color = new Color(activeItemR, activeItemG, activeItemB, 1);
        menuItemActiveParameter.borderWidth = 2;
        menuItemActiveParameter.borderColor = Color.WHITE;
        menuItemActiveParameter.shadowColor = Color.WHITE;
        menuItemActiveParameter.shadowOffsetX = -4;
        menuItemActiveParameter.shadowOffsetY = 4;
        menuItemActiveParameter.kerning = true;
        fontEstrogenMenuPlayActive = menuItemActiveGenerator.generateFont(menuItemActiveParameter);
        fontEstrogenMenuHelpActive = menuItemActiveGenerator.generateFont(menuItemActiveParameter);
        fontEstrogenMenuExitActive = menuItemActiveGenerator.generateFont(menuItemActiveParameter);
        menuItemActiveGenerator.dispose();
    }

    public void show() {

    }

    public void hide() {

    }

    public void render(float delta) {
        batch.begin();

        //Draw background textures on the screen
        batch.draw(staticBgTexture, 0, 0);
        batch.draw(scrollingMgTextureFirst, scrollingMgTextureFirstX, 0);
        batch.draw(scrollingMgTextureSecond, scrollingMgTextureSecondX, 0);
        batch.draw(scrollingFgTextureSimple, scrollingFgTextureSimpleX, 0);
        batch.draw(scrollingFgTextureDecorated, scrollingFgTextureDecoratedX, 0);

        if (showHelp) {
            //Show help text

            //batch.draw(helpDialogTexture, 25, 25);
            fontEstrogenTitle.draw(batch, "How To Play", 150, 550);
            fontEstrogenMenuPlayActive.draw(batch, "Back", 350, 200);
        } else {
            //Show the normal menu

            //Draw the title logo
            fontEstrogenTitle.draw(batch, "Twilight Drift", 120, 550);

            //Draw the menu options. If the current menu option is active then draw the active version instead
            switch (currentOption) {
                case PLAY :
                    fontEstrogenMenuPlayActive.draw(batch, "Play", 350, 400);
                    fontEstrogenMenuHelp.draw(batch, "Help", 350, 300);
                    fontEstrogenMenuExit.draw(batch, "Exit", 350, 200);
                    break;
                case HELP :
                    fontEstrogenMenuPlay.draw(batch, "Play", 350, 400);
                    fontEstrogenMenuHelpActive.draw(batch, "Help", 350, 300);
                    fontEstrogenMenuExit.draw(batch, "Exit", 350, 200);
                    break;
                case EXIT :
                    fontEstrogenMenuPlay.draw(batch, "Play", 350, 400);
                    fontEstrogenMenuHelp.draw(batch, "Help", 350, 300);
                    fontEstrogenMenuExitActive.draw(batch, "Exit", 350, 200);
                    break;
            }
        }



        batch.end();

        scrollForeground();
        scrollMidground();


        //--------- Handle input ---------


        if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
            if (!showHelp) {
                if (currentOption == currentOptionValues.PLAY) {
                    currentOption = currentOptionValues.HELP;
                } else if (currentOption == currentOptionValues.HELP) {
                    currentOption = currentOptionValues.EXIT;
                }
            }
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
            if (!showHelp) {
                if (currentOption == currentOptionValues.HELP) {
                    currentOption = currentOptionValues.PLAY;
                } else if (currentOption == currentOptionValues.EXIT) {
                    currentOption = currentOptionValues.HELP;
                }
            }
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            if (showHelp == false) {
                switch (currentOption) {
                    case PLAY:
                        //DebugScreen newScreen = new DebugScreen();
                        //newScreen.create();

                        GameScreen gameScreen = new GameScreen();
                        ((Game) Gdx.app.getApplicationListener()).setScreen(gameScreen);
                        this.dispose();
                        break;
                    case HELP:
                        showHelp = true;
                        break;
                    case EXIT:
                        Gdx.app.exit();
                        break;
                }
            } else {
                showHelp = false;
            }
        }
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

    public void resize(int width, int height) {

    }

    public void pause() {

    }

    public void resume() {

    }

    public void dispose() {
        staticBgTexture.dispose();
        scrollingMgTextureFirst.dispose();
        scrollingMgTextureSecond.dispose();
        scrollingFgTextureSimple.dispose();
        scrollingFgTextureDecorated.dispose();
        fontEstrogenTitle.dispose();
        fontEstrogenMenuPlay.dispose();
        fontEstrogenMenuHelp.dispose();
        fontEstrogenMenuExit.dispose();
        fontEstrogenMenuPlayActive.dispose();
        fontEstrogenMenuHelpActive.dispose();
        fontEstrogenMenuExitActive.dispose();
        batch.dispose();
    }
}
