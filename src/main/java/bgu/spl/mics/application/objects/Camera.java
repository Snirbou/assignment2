package bgu.spl.mics.application.objects;

import bgu.spl.mics.application.messages.DetectObjectsEvent;

import java.lang.reflect.Array;
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

    private ArrayList<StampedDetectedObjects> totalDetectedObjectList;
    private ArrayList<StampedDetectedObjects> delayedDetectedObjectList;

    public Camera(int id, int frequency, ArrayList<StampedDetectedObjects> totalDetectedObjectsList) {
        this.id = id;
        this.frequency = frequency;
        this.totalDetectedObjectList = totalDetectedObjectsList;
        this.delayedDetectedObjectList = new ArrayList<>();
        Status = STATUS.UP;
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

    public void setStatus(STATUS status) {
        Status = status;
    }

    public void retrieveCurrDetection(int tick) {
        for (StampedDetectedObjects stamped : totalDetectedObjectList) {
            if (stamped.getTime() == tick)
            {
                delayedDetectedObjectList.add(new StampedDetectedObjects(tick + frequency, stamped.getDetectedObjects()));
                for(DetectedObject obj : stamped.getDetectedObjects())
                    StatisticalFolder.getInstance().incrementDetectedObjects();
            }
        }
    }

    public StampedDetectedObjects getObjsAtTime(int time) {

        for (StampedDetectedObjects stamped : delayedDetectedObjectList) {
            if (stamped.getTime() == time)
                return stamped;

        }
        return null;
    }
}
