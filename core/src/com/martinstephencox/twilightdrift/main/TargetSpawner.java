package com.martinstephencox.twilightdrift.main;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.martinstephencox.twilightdrift.actors.BadTarget;
import com.martinstephencox.twilightdrift.actors.GoodTarget;
import com.martinstephencox.twilightdrift.actors.Target;
import com.martinstephencox.twilightdrift.actors.TargetConfigGenerator;
import com.martinstephencox.twilightdrift.consts.Consts;

import java.util.ArrayList;
import java.util.Random;

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
                boolean generatedGoodTarget = false;

                //Used to randomise the number of targets spawned at each difficulty level
                Random rand = new Random();
                int choice = rand.nextInt(100);

                switch(difficulty) {
                    case SINGLE:
                        config = gen.generateSingleConfig();
                        break;
                    case EASY:
                        if (choice < 25) {
                            config = gen.generateSingleConfig();
                        } else {
                            config = gen.generateEasyConfig();
                        }
                        break;
                    case MEDIUM:
                        if (choice < 25) {
                            config = gen.generateEasyConfig();
                        } else {
                            config = gen.generateMediumConfig();
                        }
                        break;
                    case HARD:
                        if (choice < 25) {
                            config = gen.generateMediumConfig();
                        } else {
                            config = gen.generateHardConfig();
                        }
                        break;
                }

                for (int i = 0; i < config.length; i++) {
                    if (config[i] == true) {

                        Target target = new BadTarget(i);

                        //25% chance to generate a good target instead
                        if (!generatedGoodTarget) {
                            choice = rand.nextInt(100);
                            if (choice < 25) {
                                target = new GoodTarget(i);
                                generatedGoodTarget = true;
                            }
                        }

                        targets.add(target);
                    }
                }

                int spawnRateChoice = rand.nextInt(100);
                int modSpawnRate;

                //Randomise the delay between each generated set of targets
                if (spawnRateChoice > 50) {
                    modSpawnRate = spawnRate;
                } else if (spawnRateChoice > 25) {
                    modSpawnRate = spawnRate + 100;
                } else {
                    modSpawnRate = spawnRate - 100;
                }

                Thread.sleep(modSpawnRate);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
