package com.martinstephencox.twilightdrift.actors;

import com.martinstephencox.twilightdrift.consts.Consts;

import java.util.Random;

/**
 * Created by Martin on 02/06/2016.
 */
public class TargetConfigGenerator {

    /**
     * Generates a configuration of targets featuring a single target
     * @return boolean[] An array contain boolean values representing where a target is placed
     */
    public boolean[] generateSingleConfig() {
        //Create config array prefilled with false values (which means no target)
        boolean[] config = new boolean[Consts.NUM_TRACKS];

        Random rand = new Random();

        //Place targets in random positions
        for (int i = 0; i < Consts.TARGET_PLACEMENT_SINGLE; i++) {
            int targetPos = rand.nextInt(Consts.NUM_TRACKS);
            config[targetPos] = true;
        }

        return config;
    }

    /**
     * Generates a configuration of targets featuring TARGET_PLACEMENT_EASY targets
     * @return boolean[] An array contain boolean values representing where a target is placed
     */
    public boolean[] generateEasyConfig() {
        //Create config array prefilled with false values (which means no target)
        boolean[] config = new boolean[Consts.NUM_TRACKS];

        Random rand = new Random();

        //Place targets in random positions
        for (int i = 0; i < Consts.TARGET_PLACEMENT_EASY; i++) {
            //Need to check if position is already taken by a target, if so try a new position
            while(true) {
                int targetPos = rand.nextInt(Consts.NUM_TRACKS);
                if (config[targetPos] != true) {
                    config[targetPos] = true;
                    break;
                }
            }
        }

        return config;
    }

    /**
     * Generates a configuration of targets featuring TARGET_PLACEMENT_MEDIUM targets
     * @return boolean[] An array contain boolean values representing where a target is placed
     */
    public boolean[] generateMediumConfig() {
        //Create config array prefilled with false values (which means no target)
        boolean[] config = new boolean[Consts.NUM_TRACKS];

        Random rand = new Random();

        //Place targets in random positions
        for (int i = 0; i < Consts.TARGET_PLACEMENT_MEDIUM; i++) {
            //Need to check if position is already taken by a target, if so try a new position
            while(true) {
                int targetPos = rand.nextInt(Consts.NUM_TRACKS);
                if (config[targetPos] != true) {
                    config[targetPos] = true;
                    break;
                }
            }
        }

        return config;
    }

    /**
     * Generates a configuration of targets featuring TARGET_PLACEMENT_HARD targets
     * @return boolean[] An array contain boolean values representing where a target is placed
     */
    public boolean[] generateHardConfig() {
        //Create config array prefilled with false values (which means no target)
        boolean[] config = new boolean[Consts.NUM_TRACKS];

        Random rand = new Random();

        //Place targets in random positions
        for (int i = 0; i < Consts.TARGET_PLACEMENT_HARD; i++) {
            //Need to check if position is already taken by a target, if so try a new position
            while(true) {
                int targetPos = rand.nextInt(Consts.NUM_TRACKS);
                if (config[targetPos] != true) {
                    config[targetPos] = true;
                    break;
                }
            }
        }

        return config;
    }
}
