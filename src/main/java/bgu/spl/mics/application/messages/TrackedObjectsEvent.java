package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;
import bgu.spl.mics.application.objects.DetectedObject;
import bgu.spl.mics.application.objects.TrackedObject;

import java.util.ArrayList;
import java.util.List;

public class TrackedObjectsEvent implements Event<Void> {

    private final ArrayList<TrackedObject> trackedObjects;
    private final int time;
    private final int lidarId;

    public TrackedObjectsEvent(ArrayList<TrackedObject> trackedObjects, int time, int lidarId) {
        this.trackedObjects = trackedObjects;
        this.time = time;
        this.lidarId = lidarId;
    }

    public ArrayList<TrackedObject> getTrackedObjects() {
        return trackedObjects;
    }

    public int getTime() {
        return time;
    }

    public int getLidarId() {
        return lidarId;
    }


}
