package bgu.spl.mics.application.objects;

import java.util.ArrayList;

/**
 * Represents the robot's GPS and IMU system.
 * Provides information about the robot's position and movement.
 */
public class GPSIMU {

    private int currentTick;
    private STATUS status;
    private ArrayList<Pose> PoseList;

    public GPSIMU(int currentTick, ArrayList<Pose> poseList) {
        this.currentTick = currentTick;
        PoseList = poseList;

        status = STATUS.UP;
    }

    public int getCurrentTick() {
        return currentTick;
    }

    public ArrayList<Pose> getPoseList() {
        return PoseList;
    }

    public void setCurrentTick(int currentTick) {
        this.currentTick = currentTick;
    }

    public void setStatus(STATUS status) {
        this.status = status;
    }

    public void incrementTick()
    {
        currentTick++;
    }
}
