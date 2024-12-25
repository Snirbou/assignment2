package bgu.spl.mics.application.objects;

import java.util.ArrayList;

/**
 * Manages the fusion of sensor data for simultaneous localization and mapping (SLAM).
 * Combines data from multiple sensors (e.g., LiDAR, camera) to build and update a global map.
 * Implements the Singleton pattern to ensure a single instance of FusionSlam exists.
 */
public class FusionSlam {

    private final static FusionSlam INSTANCE = new FusionSlam();

    private ArrayList<LandMark> landmark;
    private ArrayList<Pose> poses;

    private FusionSlam()
    {
        landmark = new ArrayList<>();
        poses = new ArrayList<>();
    }

    public ArrayList<LandMark> getLandmark() {
        return landmark;
    }

    public ArrayList<Pose> getPoses() {
        return poses;
    }

    public static FusionSlam getInstance()
    {
        return INSTANCE;
    }



}

