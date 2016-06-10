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
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.martinstephencox.twilightdrift.actors.Player;
import com.martinstephencox.twilightdrift.actors.TargetConfigGenerator;
import com.martinstephencox.twilightdrift.audio.BackgroundMusicPlayer;
import com.martinstephencox.twilightdrift.interfaces.PlayerInterface;
import com.martinstephencox.twilightdrift.main.ScoreThread;
import com.martinstephencox.twilightdrift.consts.Consts;


/**
 * Created by Martin on 31/05/2016.
 */
public class DebugScreen implements Screen {

    private OrthographicCamera camera;
    private Viewport viewport;
    private SpriteBatch batch;
    private Texture img;
    private BitmapFont fontEstrogen;

    //Using the PlayerInterface to access Player class
    private PlayerInterface player = Player.getPlayer();
    private ScoreThread scoreThread = new ScoreThread(player);

    private BackgroundMusicPlayer bgm = new BackgroundMusicPlayer();
    Sound cashSFX = Gdx.audio.newSound(Gdx.files.internal(Consts.SFX_CASH_POINTS));
    Sound hitSFX = Gdx.audio.newSound(Gdx.files.internal(Consts.SFX_HIT_BAD));

    public void create() {

        //Create the orthographic camera
        int screenWidth = Gdx.graphics.getWidth();
        int screenHeight = Gdx.graphics.getHeight();
        camera = new OrthographicCamera(screenWidth, screenHeight);
        camera.position.set(camera.viewportWidth /2f, camera.viewportHeight / 2f, 0);
        camera.update();
        viewport = new FitViewport(800, 600, camera);

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

    public void render(float delta) {
        camera.update();
        batch.setProjectionMatrix(camera.combined);
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
        fontEstrogen.draw(batch, "Total Score: " + player.getCurrentTotalScore(), 10, 450);
        fontEstrogen.draw(batch, "Chunk Score: " + player.getCurrentChunkScore(), 10, 400);
        fontEstrogen.draw(batch, "Multiplier: " + player.getCurrentMultiplier(), 10, 350);
        fontEstrogen.draw(batch, "Character pos: " + player.getCurrentPos(), 200, 200);
        batch.end();
    }

    public void show() {

    }

    @Override
    public void resize(int width, int height) {
        //camera.viewportWidth = width;
        //camera.viewportHeight = height;
        //camera.update();
        viewport.update(width, height);
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

}
