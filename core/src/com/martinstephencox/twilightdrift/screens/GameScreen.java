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
import com.martinstephencox.twilightdrift.actors.*;
import com.martinstephencox.twilightdrift.audio.BackgroundMusicPlayer;
import com.martinstephencox.twilightdrift.consts.Consts;
import com.martinstephencox.twilightdrift.interfaces.PlayerInterface;
import com.martinstephencox.twilightdrift.main.ScoreThread;
import com.martinstephencox.twilightdrift.main.TargetSpawner;

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
    private int scoreIncreaseScrollValue = Consts.INCREASE_DIFFICULTY_VALUE;

    private enum difficultyRange {SINGLE, EASY, MEDIUM, HARD};

    private difficultyRange difficulty = difficultyRange.SINGLE;

    private SpriteBatch batch;
    private BitmapFont fontEstrogen;

    //Using the PlayerInterface to access Player class
    private PlayerInterface player = Player.getPlayer();
    private ScoreThread scoreThread = new ScoreThread(player);

    private BackgroundMusicPlayer bgm = new BackgroundMusicPlayer();
    Sound cashSFX = Gdx.audio.newSound(Gdx.files.internal(Consts.SFX_CASH_POINTS));
    Sound hitSFX = Gdx.audio.newSound(Gdx.files.internal(Consts.SFX_HIT_BAD));

    private ArrayList<Target> targets = new ArrayList<>();
    private TargetConfigGenerator generator = new TargetConfigGenerator();
    private TargetSpawner spawner;

    private boolean collisionTimedOut = false;

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
        spawner = new TargetSpawner(targets, batch);
        new Thread(spawner).start();
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

        //TODO: REMOVE ALL THE DEBUG OPERATIONS

        //  ---------- DEBUG OPERATIONS ----------

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
            //createBadTargetConfig();
        }

        //Debug to change volume bgm
        if (Gdx.input.isKeyJustPressed(Input.Keys.O)) {
            bgm.setVolume(0.2f);
        }

        //  ---------- END DEBUG OPERATIONS ----------

        batch.begin();
        batch.draw(midTextureFirst, 0, midTextureFirstY);
        batch.draw(midTextureSecond, 0, midTextureSecondY);
        batch.draw(waveTexture, 0, 0);
        batch.end();

        //Add TargetSpawner threads generated targets to render threads targets
        targets.addAll(spawner.getTargets());

        //Clear the TargetSpawner threads generated target list to prevent duplicate targets being spawned
        spawner.clearTargets();

        //Spawn and redraw all targets
        for (Target t: targets) {
            if (t.isSpawned()) {
                //Target already spawned, just need to move it
                t.redraw(batch, scrollRate + 1);
            } else {
                //Need to spawn target first
                t.spawn(batch);
            }
        }

        batch.begin();
        batch.draw(player.getTexture(), player.getAdjustedX(), player.getY());
        fontEstrogen.draw(batch, "Total Score: " + player.getCurrentTotalScore(), 10, 450);
        fontEstrogen.draw(batch, "Chunk Score: " + player.getCurrentChunkScore(), 10, 400);
        fontEstrogen.draw(batch, "Multiplier: " + player.getCurrentMultiplier(), 10, 350);
        batch.end();


        //Increase the background scroll of the image at increasingly higher scores
        //Increase the rate of spawn of targets at increasingly higher scores
        //Increase the difficulty of generated targets at increasingly higher scores
        if (scrollRate < maxScrollRate) {
            if (player.getCurrentTotalScore() > scoreIncreaseScrollValue) {
                scoreIncreaseScrollValue = scoreIncreaseScrollValue * 3;
                scrollRate++;
                spawner.setSpawnRate(spawner.getSpawnRate() - 100);
                spawner.increaseDifficulty();
            }
        }

        scrollMidground();
        checkTargetCollision();
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
        waveTexture.dispose();
        midTextureFirst.dispose();
        midTextureSecond.dispose();
        cashSFX.dispose();
        hitSFX.dispose();
        bgm.dispose();
        batch.dispose();
    }

    /**
     * Checks whether the player has collided with any targets
     */
    private void checkTargetCollision() {
        if (!collisionTimedOut) {
            for (Target t : targets) {
                if (t.getX() == player.getX()) {
                    //In the same column

                    //TODO: This will only trigger if the player and the bad target are overlapping each other exactly.
                    //TODO: Implement bounding box collision detection only for the Y axis (badTarget overlapping player at any value of Y)
                    if (t.getY() == player.getY()) {
                        if (t instanceof BadTarget) {
                            playerHitBad();
                            setCollisionTimedOut();
                            break;
                        } else if (t instanceof GoodTarget) {
                            playerHitGood();
                            break;
                        }
                    }

                    /*if (t.getY() + t.getHeight()/2 < player.getY() + player.getTexture().getHeight()/2) {
                        if (t.getY() + t.getHeight()/2 < player.getY() - player.getTexture().getHeight()/2) {
                            playerHitBad();
                        }
                    }*/
                }
            }
        }
    }

    /**
     * The player hit a bad target, process all game logic for hitting a bad target
     */
    private void playerHitBad() {
        player.resetChunkScore();
        player.resetMultiplier();
        player.decrementLives();
        hitSFX.play();
        bgm.lowerVolumeOnHit();
        scoreThread.pauseScoreThread();
    }

    private void playerHitGood() {
        player.incrementMultiplier();
        player.updateChunkScore();
    }

    private void setCollisionTimedOut() {
        collisionTimedOut = true;
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                collisionTimedOut = false;
            }
        }, Consts.PAUSE_VALUE);
    }

    /**
     * Move textures down at scrollRate pixels per frame
     */
    public void scrollMidground() {
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
}
