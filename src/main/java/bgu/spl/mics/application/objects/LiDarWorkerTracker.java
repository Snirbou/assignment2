package bgu.spl.mics.application.objects;

import java.util.ArrayList;

/**
 * LiDarWorkerTracker is responsible for managing a LiDAR worker.
 * It processes DetectObjectsEvents and generates TrackedObjectsEvents by using data from the LiDarDataBase.
 * Each worker tracks objects and sends observations to the FusionSlam service.
 */
public class LiDarWorkerTracker {

    private int id;
    private int frequency;
    private STATUS Status;
    private ArrayList<TrackedObject> lastTrackedObjects;

    public LiDarWorkerTracker(int id, int frequency, ArrayList<TrackedObject> lastTrackedObjects) {
        this.id = id;
        this.frequency = frequency;
        this.lastTrackedObjects = lastTrackedObjects;

        this.Status = STATUS.UP;
    }

    public int getId() {
        return id;
    }

    public int getFrequency() {
        return frequency;
    }

    public ArrayList<TrackedObject> getLastTrackedObjects() {
        return lastTrackedObjects;
    }

    public void SetStatus(STATUS status) {
        Status = status;
    }
}
