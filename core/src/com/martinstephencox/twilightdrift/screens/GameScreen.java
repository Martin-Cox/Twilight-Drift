package com.martinstephencox.twilightdrift.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.martinstephencox.twilightdrift.actors.Player;
import com.martinstephencox.twilightdrift.audio.BackgroundMusicPlayer;
import com.martinstephencox.twilightdrift.consts.Consts;
import com.martinstephencox.twilightdrift.interfaces.PlayerInterface;
import com.martinstephencox.twilightdrift.main.ScoreThread;

/**
 * Created by Martin on 05/06/2016.
 */
public class GameScreen implements Screen {

    private Texture waveTexture = new Texture(Gdx.files.internal(Consts.IMAGE_GAME_WAVES));

    private SpriteBatch batch;
    private BitmapFont fontEstrogen;

    //Using the PlayerInterface to access Player class
    private PlayerInterface player = Player.getPlayer();
    private ScoreThread scoreThread = new ScoreThread(player);

    private BackgroundMusicPlayer bgm = new BackgroundMusicPlayer();
    Sound cashSFX = Gdx.audio.newSound(Gdx.files.internal(Consts.SFX_CASH_POINTS));
    Sound hitSFX = Gdx.audio.newSound(Gdx.files.internal(Consts.SFX_HIT_BAD));


    public GameScreen() {
        batch = new SpriteBatch();

        //Load up nice font
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(Consts.FONT_ESTROGEN));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 36;
        parameter.color = Color.BLUE;
        parameter.borderWidth = 1;
        parameter.borderColor = Color.WHITE;
        parameter.kerning = true;
        fontEstrogen = generator.generateFont(parameter);
        generator.dispose();

        //Start updating chunk score
        player.startChunkScore(scoreThread);

        //Start playing background music
        bgm.startMusic();
    }

    public void show() {

    }

    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //Move left
        if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT) || Gdx.input.isKeyJustPressed(Input.Keys.A)) {
            player.updatePos(Consts.MOVEMENT.LEFT);
        }

        //Move right
        if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT) || Gdx.input.isKeyJustPressed(Input.Keys.D)) {
            player.updatePos(Consts.MOVEMENT.RIGHT);
        }

        //Reset to centre
        if (Gdx.input.isKeyJustPressed(Input.Keys.UP) || Gdx.input.isKeyJustPressed(Input.Keys.W)) {
            player.updatePos(Consts.MOVEMENT.RECENTER);
        }

        //Debug to add chunk score to total and reset chunk score
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            cashSFX.play();
            player.updateTotalScore();
            player.resetChunkScore();
            player.resetMultiplier();
        }

        //Debug to add multiplier
        if (Gdx.input.isKeyJustPressed(Input.Keys.CONTROL_LEFT)) {
            player.incrementMultiplier();
        }

        //Debug to pause chunk score and reset
        if (Gdx.input.isKeyJustPressed(Input.Keys.SHIFT_LEFT)) {
            onPlayerHitBad();
        }

        //Debug to pause bgm
        if (Gdx.input.isKeyJustPressed(Input.Keys.P)) {
            if (bgm.isPlaying()) {
                bgm.pause();
            } else {
                bgm.resume();
            }

        }

        //Debug to change volume bgm
        if (Gdx.input.isKeyJustPressed(Input.Keys.O)) {
            bgm.setVolume(0.2f);
        }

        batch.begin();
        batch.draw(waveTexture, 0, 0);
        batch.draw(player.getTexture(), player.getAdjustedX(), player.getY());
        fontEstrogen.draw(batch, "Total Score: " + player.getCurrentTotalScore(), 10, 450);
        fontEstrogen.draw(batch, "Chunk Score: " + player.getCurrentChunkScore(), 10, 400);
        fontEstrogen.draw(batch, "Multiplier: " + player.getCurrentMultiplier(), 10, 350);
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

    private void onPlayerHitBad() {
        player.resetChunkScore();
        player.resetMultiplier();
        hitSFX.play();
        bgm.lowerVolumeOnHit();
        scoreThread.pauseScoreThread();
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
