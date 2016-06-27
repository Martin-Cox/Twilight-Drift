package com.martinstephencox.twilightdrift.screens;

import com.badlogic.gdx.Game;
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
    private BitmapFont fontTotalScore;
    private BitmapFont fontChunkScore;
    private BitmapFont fontMultiplier;

    //Using the PlayerInterface to access Player class
    private PlayerInterface player = Player.getPlayer();
    private ScoreThread scoreThread = new ScoreThread(player);

    private BackgroundMusicPlayer bgm = new BackgroundMusicPlayer();
    Sound cashSFX = Gdx.audio.newSound(Gdx.files.internal(Consts.SFX_CASH_POINTS));
    Sound hitBadSFX = Gdx.audio.newSound(Gdx.files.internal(Consts.SFX_HIT_BAD));
    Sound hitGoodSFX = Gdx.audio.newSound(Gdx.files.internal(Consts.SFX_CASH_POINTS));

    private ArrayList<Target> targets = new ArrayList<>();
    private TargetConfigGenerator generator = new TargetConfigGenerator();
    private TargetSpawner spawner;

    private boolean collisionTimedOut = false;

    private float scoreR = 0/255f;
    private float scoreG = 151/255f;
    private float scoreB = 157/255f;

    private float multiplierR = 189/255f;
    private float multiplierG = 13/255f;
    private float multiplierB = 97/255f;

    public GameScreen() {
        batch = new SpriteBatch();

        //Load up total score font
        FreeTypeFontGenerator totalScoreGenerator = new FreeTypeFontGenerator(Gdx.files.internal(Consts.FONT_ESTROGEN));
        FreeTypeFontGenerator.FreeTypeFontParameter totalScoreParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        totalScoreParameter.size = 56;
        totalScoreParameter.color = new Color(scoreR, scoreG, scoreB, 1);
        totalScoreParameter.borderWidth = 2;
        totalScoreParameter.borderColor = Color.WHITE;
        totalScoreParameter.shadowColor = Color.WHITE;
        totalScoreParameter.shadowOffsetX = -4;
        totalScoreParameter.shadowOffsetY = 4;
        totalScoreParameter.kerning = true;
        fontTotalScore = totalScoreGenerator.generateFont(totalScoreParameter);
        totalScoreGenerator.dispose();

        //Load up chunk score font
        FreeTypeFontGenerator chunkScoreGenerator = new FreeTypeFontGenerator(Gdx.files.internal(Consts.FONT_ESTROGEN));
        FreeTypeFontGenerator.FreeTypeFontParameter chunkScoreParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        chunkScoreParameter.size = 36;
        chunkScoreParameter.color = new Color(scoreR, scoreG, scoreB, 1);
        chunkScoreParameter.borderWidth = 2;
        chunkScoreParameter.borderColor = Color.WHITE;
        chunkScoreParameter.shadowColor = Color.WHITE;
        chunkScoreParameter.shadowOffsetX = -4;
        chunkScoreParameter.shadowOffsetY = 4;
        chunkScoreParameter.kerning = true;
        fontChunkScore = chunkScoreGenerator.generateFont(chunkScoreParameter);
        chunkScoreGenerator.dispose();

        //Load up multiplier font
        FreeTypeFontGenerator multiplierGenerator = new FreeTypeFontGenerator(Gdx.files.internal(Consts.FONT_ESTROGEN));
        FreeTypeFontGenerator.FreeTypeFontParameter multiplierParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        multiplierParameter.size = 36;
        multiplierParameter.color = new Color(multiplierR, multiplierG, multiplierB, 1);
        multiplierParameter.borderWidth = 2;
        multiplierParameter.borderColor = Color.WHITE;
        multiplierParameter.shadowColor = Color.WHITE;
        multiplierParameter.shadowOffsetX = -4;
        multiplierParameter.shadowOffsetY = 4;
        multiplierParameter.kerning = true;
        fontMultiplier = multiplierGenerator.generateFont(multiplierParameter);
        multiplierGenerator.dispose();

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

        //Cash in current score
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            cashSFX.play();
            player.updateTotalScore();
            player.resetChunkScore();
            player.resetMultiplier();
        }

        //Debug to pause bgm
        /*if (Gdx.input.isKeyJustPressed(Input.Keys.P)) {
            if (bgm.isPlaying()) {
                bgm.pause();
            } else {
                bgm.resume();
            }
        }*/

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
        try {
            for (Target t : targets) {
                if (t.isSpawned()) {
                    //Target already spawned, just need to move it
                    t.redraw(batch, scrollRate + 1);
                } else {
                    //Need to spawn target first
                    t.spawn(batch);
                }
            }
        } catch (NullPointerException e) {

        }

        batch.begin();
        batch.draw(player.getTexture(), player.getAdjustedX(), player.getY());
        fontTotalScore.draw(batch, getFormattedTotalScore(), 210, 575);
        fontChunkScore.draw(batch, getFormattedChunkScore(), 310, 510);
        fontMultiplier.draw(batch, getFormattedMultiplier(), 420, 510);
        fontMultiplier.draw(batch, "Lives left: " + player.getLives(), 10, 50);
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

        //Has player run out of lives, if so move to game over screen
        if (player.getLives() <= 0 ) {
            GameOverScreen gameOverScreen = new GameOverScreen();
            ((Game) Gdx.app.getApplicationListener()).setScreen(gameOverScreen);
            this.dispose();
        }
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
        fontTotalScore.dispose();
        fontChunkScore.dispose();
        fontMultiplier.dispose();
        cashSFX.dispose();
        hitBadSFX.dispose();
        hitGoodSFX.dispose();
        bgm.dispose();
        batch.dispose();
    }

    /**
     * Checks whether the player has collided with any targets
     */
    private void checkTargetCollision() {

        int playerMaxY = player.getY() + player.getTexture().getHeight()/2 - 30; //The uppermost pixel of the player sprite (-30 pixels to allow for transparent border spacing)
        int playerMinY = player.getY() - player.getTexture().getHeight()/2; //The lowermost pixel of the player sprite

        boolean isColliding = false;

        if (!collisionTimedOut) {
            for (Target t : targets) {
                if (t.getX() == player.getX()) {
                    //In the same column

                    int targetMaxY = t.getY() + t.getHeight()/2; //The uppermost pixel of the player sprite
                    int targetMinY = t.getY() - t.getHeight()/2; //The lowermost pixel of the player sprite

                    //If the bottom of the target is lower than the top of the player and the top of the target is above the bottom of the player
                    if ((targetMinY <= playerMaxY) && targetMaxY > playerMinY) {
                        if (t instanceof BadTarget) {
                            playerHitBad();
                            setCollisionTimedOut();
                            targets.remove(t);
                            break;
                        } else if (t instanceof GoodTarget) {
                            playerHitGood();
                            targets.remove(t);
                            break;
                        }
                    }
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
        hitBadSFX.play();
        bgm.lowerVolumeOnHit();
        scoreThread.pauseScoreThread();
    }

    /**
     * Player hit a good target, process all game logic for hitting a good target
     */
    private void playerHitGood() {
        player.incrementMultiplier();
        player.updateChunkScore();
        hitGoodSFX.play();
    }

    /**
     * Set a collision flag to false for Consts.PAUSE_VALUE ms
     */
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
     * Prepends the players total score with zeros
     * @return A formatted string containing the total score
     */
    private String getFormattedTotalScore(){
        String score = "" + player.getCurrentTotalScore();

        String formattedScore = ("0000000000" + score).substring(score.length());

        return formattedScore;
    }

    /**
     * Prepends the players chunk score with zeros
     * @return A formatted string containing the chunk score
     */
    private String getFormattedChunkScore(){
        String score = "" + player.getCurrentChunkScore();

        return "" + ("0000" + score).substring(score.length());
    }

    /**
     * Prepends the players multiplier with zeros
     * @return A formatted string containing the multiplier
     */
    private String getFormattedMultiplier(){
        String mult = "" + player.getCurrentMultiplier();

        return "x " + ("00" + mult).substring(mult.length());
    }

    /**
     * Move textures down at scrollRate pixels per frame
     */
    private void scrollMidground() {
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
