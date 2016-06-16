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
import com.martinstephencox.twilightdrift.actors.BadTarget;
import com.martinstephencox.twilightdrift.actors.Player;
import com.martinstephencox.twilightdrift.actors.Target;
import com.martinstephencox.twilightdrift.actors.TargetConfigGenerator;
import com.martinstephencox.twilightdrift.audio.BackgroundMusicPlayer;
import com.martinstephencox.twilightdrift.consts.Consts;
import com.martinstephencox.twilightdrift.interfaces.PlayerInterface;
import com.martinstephencox.twilightdrift.main.ScoreThread;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Martin on 05/06/2016.
 */
public class GameScreen implements Screen {

    private Texture waveTexture = new Texture(Gdx.files.internal(Consts.IMAGE_GAME_WAVES));
    private Texture midTextureFirst = new Texture(Gdx.files.internal(Consts.IMAGE_SCROLLING_GAME_MIDGROUND));
    private Texture midTextureSecond = new Texture(Gdx.files.internal(Consts.IMAGE_SCROLLING_GAME_MIDGROUND));

    private int midTextureFirstY = 0;
    private int midTextureSecondY = Consts.GAME_MIDGROUND_HEIGHT;

    private int spawnRate = 1000; //1 second
    private int scrollRate = 1;
    private int maxScrollRate = 8;
    private int scoreIncreaseScrollValue = 5000;

    private SpriteBatch batch;
    private BitmapFont fontEstrogen;

    //Using the PlayerInterface to access Player class
    private PlayerInterface player = Player.getPlayer();
    private ScoreThread scoreThread = new ScoreThread(player);

    private BackgroundMusicPlayer bgm = new BackgroundMusicPlayer();
    Sound cashSFX = Gdx.audio.newSound(Gdx.files.internal(Consts.SFX_CASH_POINTS));
    Sound hitSFX = Gdx.audio.newSound(Gdx.files.internal(Consts.SFX_HIT_BAD));

    private ArrayList<Target> badTargets = new ArrayList<>();
    private TargetConfigGenerator generator = new TargetConfigGenerator();

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

        //Start spawning bad targets
        spawnBadTargets();

    }

    public void show() {

    }

    public void render(float delta) {
        Gdx.gl.glClearColor(127/255f, 168/255f, 1, 1);
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
            playerHitBad();
        }

        //Debug to pause bgm
        if (Gdx.input.isKeyJustPressed(Input.Keys.P)) {
            if (bgm.isPlaying()) {
                bgm.pause();
            } else {
                bgm.resume();
            }
        }

        //Debug to create more bad targets
        if (Gdx.input.isKeyJustPressed(Input.Keys.T)) {
            spawnBadTargets();
        }

        //Debug to change volume bgm
        if (Gdx.input.isKeyJustPressed(Input.Keys.O)) {
            bgm.setVolume(0.2f);
        }

        batch.begin();
        batch.draw(midTextureFirst, 0, midTextureFirstY);
        batch.draw(midTextureSecond, 0, midTextureSecondY);
        batch.draw(waveTexture, 0, 0);
        batch.end();

        //Redraw all bad targets
        for (Target t: badTargets) {
            t.redraw(batch, scrollRate);
        }

        batch.begin();
        batch.draw(player.getTexture(), player.getAdjustedX(), player.getY());
        fontEstrogen.draw(batch, "Total Score: " + player.getCurrentTotalScore(), 10, 450);
        fontEstrogen.draw(batch, "Chunk Score: " + player.getCurrentChunkScore(), 10, 400);
        fontEstrogen.draw(batch, "Multiplier: " + player.getCurrentMultiplier(), 10, 350);
        batch.end();


        //Increase the background scroll of the image at increasingly higher scores
        if (scrollRate < maxScrollRate) {
            if (player.getCurrentTotalScore() > scoreIncreaseScrollValue) {
                scoreIncreaseScrollValue = scoreIncreaseScrollValue * 2;
                scrollRate++;
            }
        }

        scrollMidground();
        checkBadTargetCollision();

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

    private void spawnBadTargets() {
        boolean[] config = generator.generateMediumConfig();
        for (int i = 0; i < config.length; i++) {
            if (config[i] == true) {
                BadTarget target = new BadTarget();
                badTargets.add(target);
                target.spawn(batch, i);
            }
        }
    }

    private void checkBadTargetCollision() {
        for (Target t : badTargets) {
            if (t.getX() == player.getX()) {
                //In the same column

                //TODO: This will only trigger if the player and the bad target are overlapping each other exactly.
                //TODO: Implement bounding box collision detection only for the Y axis (badTarget overlapping player at any value of Y)
                if (t.getY() == player.getY()) {
                    playerHitBad();
                }
            }
        }

    }

    private void playerHitBad() {
        player.resetChunkScore();
        player.resetMultiplier();
        hitSFX.play();
        bgm.lowerVolumeOnHit();
        scoreThread.pauseScoreThread();
    }

    public void scrollMidground() {
        //Move textures down at scrollRate pixels per frame
        midTextureFirstY -= scrollRate;
        midTextureSecondY -= scrollRate;

        //If first texture goes out of view via the bottom edge of the window, move it to the top edge of window to give impression of infinite scroll
        if ((midTextureFirstY) < -Consts.GAME_MIDGROUND_HEIGHT) {
            midTextureFirstY = Consts.GAME_MIDGROUND_HEIGHT - scrollRate;
        }

        //If second texture goes out of view via the bottom edge of the window, move it to the top edge of window to give impression of infinite scroll
        if ((midTextureSecondY) < -Consts.GAME_MIDGROUND_HEIGHT) {
            midTextureSecondY = Consts.GAME_MIDGROUND_HEIGHT - scrollRate;
        }
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
