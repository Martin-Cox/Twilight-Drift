package com.martinstephencox.twilightdrift.consts;

/**
 * Created by Martin on 31/05/2016.
 */
public class Consts {

    //Camera/world values
    public static final int CAMERA_WIDTH = 800;
    public static final int CAMERA_HEIGHT = 600;

    //Animation values
    public static final int MENU_FOREGROUND_SCROLL_RATE = 7;
    public static final int MENU_FOREGROUND_WIDTH = 800;
    public static final int MENU_MIDGROUND_SCROLL_RATE = 2;
    public static final int MENU_MIDGROUND_WIDTH = 1600;
    public static final int GAME_MIDGROUND_HEIGHT = 600;

    //Actor positioning on "tracks"
    public static final int LEFT_POS = 200;
    public static final int MID_LEFT_POS = 300;
    public static final int CENTER_POS = 400;
    public static final int MID_RIGHT_POS = 500;
    public static final int RIGHT_POS = 600;
    public static final int TRACK_DIFFERENCE = 100; //The difference in pixels between the center of each track

    //Track bounds
    public static final int LEFT_BOUND = 0;
    public static final int RIGHT_BOUND = 4;
    public static final int CENTER_TRACK = 2;
    public static final int NUM_TRACKS = 5;


    //Target configurations
    public static final int TARGET_PLACEMENT_SINGLE = 1;
    public static final int TARGET_PLACEMENT_EASY = 2;
    public static final int TARGET_PLACEMENT_MEDIUM = 3;
    public static final int TARGET_PLACEMENT_HARD = 4;

    //Space (in rows) between targets on "tracks"
    public static final int SPACING_EASY = 6;
    public static final int SPACING_MEDIUM = 4;
    public static final int SPACING_HARD = 2;

    //Player movement
    public enum MOVEMENT {LEFT, RIGHT, RECENTER}

    //Player lives
    public static final int STARTING_LIVES = 3;
    public static final int MAX_LIVES = 10;

    //Score values
    public static final int MAX_TOTAL_SCORE = Integer.MAX_VALUE;
    public static final int MAX_CHUNK_SCORE = 9999;
    public static final int MIN_SCORE_MULTIPLIER = 1;
    public static final int MAX_SCORE_MULTIPLIER = 99;
    public static final int GOOD_TARGET_VALUE = 500;
    public static final int UPDATE_SCORE_RATE = 20; //Update score 20 times a second
    public static final int PAUSE_VALUE = 2000; //Prevent score from increasing for 1.5 seconds after hitting a bad object

    //Assets
    public static final String MUSIC_DIRECTORY = "music/";
    public static final String SFX_HIT_BAD = "sfx/hit_bad_alt.mp3";
    public static final String SFX_CASH_POINTS = "sfx/cash_in.mp3";
    public static final String FONT_ESTROGEN = "fonts/ESTROGEN.ttf";
    public static final String IMAGE_STATIC_MENU_BACKGROUND = "images/menu/menu_static_bg.png";
    public static final String IMAGE_SCROLLING_MENU_FOREGROUND_1 = "images/menu/menu_scrolling_bg_transparent_1.png";
    public static final String IMAGE_SCROLLING_MENU_FOREGROUND_2 = "images/menu/menu_scrolling_bg_transparent_2.png";
    public static final String IMAGE_SCROLLING_MENU_MIDGROUND = "images/menu/menu_scrolling_city_transparent.png";
    public static final String IMAGE_HELP_DIALOG = "images/menu/help_dialog.png";
    public static final String IMAGE_GAME_WAVES = "images/game/waves_transparent.png";
    public static final String IMAGE_SCROLLING_GAME_MIDGROUND = "images/game/game_mid_transparent.png";
    public static final String IMAGE_PLAYER = "images/game/player_transparent.png";

    //Default Options
    public static final float MUSIC_VOLUME = 1.0f;
    public static final float HIT_VOLUME_VALUE = 0.4f; //The volume to temporarily set bgm to when player hits a bad object

}
