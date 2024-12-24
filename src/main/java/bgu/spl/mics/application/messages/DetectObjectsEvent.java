package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;
import bgu.spl.mics.application.objects.DetectedObject;
import bgu.spl.mics.application.objects.Pose;
import bgu.spl.mics.application.objects.TrackedObject;

import java.util.List;

public class DetectObjectsEvent implements Event<List<TrackedObject>> {

    private final List<DetectedObject> detectedObjects;
    private final int time;
    private final int cameraId;

    public DetectObjectsEvent(List<DetectedObject> detectedObjects, int time, int cameraId) {
        this.detectedObjects = detectedObjects;
        this.time = time;
        this.cameraId = cameraId;
    }

    public List<DetectedObject> getDetectedObjects() {
        return detectedObjects;
    }

    public int getTime() {
        return time;
    }

    public int getCameraId() {
        return cameraId;
    }

}
