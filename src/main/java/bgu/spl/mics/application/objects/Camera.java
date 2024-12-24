package bgu.spl.mics.application.objects;

import bgu.spl.mics.application.messages.DetectObjectsEvent;

import java.util.ArrayList;

/**
 * Represents a camera sensor on the robot.
 * Responsible for detecting objects in the environment.
 */
public class Camera {


    private int id;
    private int frequency;
    private enum Status {
        UP,
        DOWN,
        ERROR
    };

    private ArrayList<StampedDetectedObjects> totalDetectedObjectList;


    public Camera(int id, int frequency, ArrayList<StampedDetectedObjects> detectedObjectList) {
        this.id = id;
        this.frequency = frequency;
        this.totalDetectedObjectList = detectedObjectList;
    }

    public int getId() {
        return id;
    }

    public int getFrequency() {
        return frequency;
    }

    public ArrayList<StampedDetectedObjects> getDetectedObjectList() {
        return totalDetectedObjectList;
    }

    public DetectObjectsEvent handleTick(int tick)
    {
        StampedDetectedObjects currObjects = totalDetectedObjectList.get(tick);
        return new DetectObjectsEvent(currObjects.getDetectedObjects(), tick, id);
    }
}
