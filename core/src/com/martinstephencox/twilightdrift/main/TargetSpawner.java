package com.martinstephencox.twilightdrift.main;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.martinstephencox.twilightdrift.actors.BadTarget;
import com.martinstephencox.twilightdrift.actors.Target;
import com.martinstephencox.twilightdrift.actors.TargetConfigGenerator;
import com.martinstephencox.twilightdrift.consts.Consts;

import java.util.ArrayList;

/**
 * Created by Martin on 17/06/2016.
 */
public class TargetSpawner implements Runnable {

    private ArrayList<Target> targets = new ArrayList<>();
    private TargetConfigGenerator gen = new TargetConfigGenerator();
    private int spawnRate = 1000;
    private Batch batch;
    private boolean isPaused = false;
    private Consts.DIFFICULTY difficulty = Consts.DIFFICULTY.SINGLE;

    public TargetSpawner(ArrayList<Target> bt, Batch b) {
        batch = b;
    }

    public void setSpawnRate(int rate) {
        spawnRate = rate;
    }

    public int getSpawnRate() { return spawnRate; }

    public void setDifficulty(Consts.DIFFICULTY d) {
        difficulty = d;
    }

    public void increaseDifficulty() {
        switch(difficulty) {
            case SINGLE:
                difficulty = Consts.DIFFICULTY.EASY;
                break;
            case EASY:
                difficulty = Consts.DIFFICULTY.MEDIUM;
                break;
            case MEDIUM:
                difficulty = Consts.DIFFICULTY.HARD;
                break;
        }
    }

    public ArrayList<Target> getTargets() {
        return targets;
    }

    public void clearTargets() {
        targets.clear();
    }

    @Override
    public void run() {
        while(true) {
            try {
                boolean[] config = new boolean[Consts.NUM_TRACKS];

                switch(difficulty) {
                    case SINGLE:
                        config = gen.generateSingleConfig();
                        break;
                    case EASY:
                        config = gen.generateEasyConfig();
                        break;
                    case MEDIUM:
                        config = gen.generateMediumConfig();
                        break;
                    case HARD:
                        config = gen.generateHardConfig();
                        break;
                }

                for (int i = 0; i < config.length; i++) {
                    if (config[i] == true) {
                        //TODO: Here add a 25% chance to create a single good target per configuration
                        Target target = new BadTarget(i);
                        targets.add(target);
                    }
                }

                Thread.sleep(spawnRate);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
