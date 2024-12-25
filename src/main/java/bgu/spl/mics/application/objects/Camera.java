package bgu.spl.mics.application.objects;

import bgu.spl.mics.application.messages.DetectObjectsEvent;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Represents a camera sensor on the robot.
 * Responsible for detecting objects in the environment.
 */
public class Camera {


    private int id;
    private int frequency;
    private STATUS Status;

    private ConcurrentHashMap<Integer, StampedDetectedObjects> totalDetectedObjectMap;


    public Camera(int id, int frequency, ConcurrentHashMap<Integer, StampedDetectedObjects> totalDetectedObjectMap) {
        this.id = id;
        this.frequency = frequency;
        this.totalDetectedObjectMap = totalDetectedObjectMap;

        Status = STATUS.UP;
    }

    public int getId() {
        return id;
    }

    public int getFrequency() {
        return frequency;
    }

    public ConcurrentHashMap<Integer, StampedDetectedObjects> getDetectedObjectList() {
        return totalDetectedObjectMap;
    }

    public void setStatus(STATUS status) {
        Status = status;
    }

    //    public DetectObjectsEvent handleTick(int tick)
//    {
//        StampedDetectedObjects currObjects = totalDetectedObjectList.get(tick);
//        return new DetectObjectsEvent(currObjects.getDetectedObjects(), tick, id);
//    }
}
