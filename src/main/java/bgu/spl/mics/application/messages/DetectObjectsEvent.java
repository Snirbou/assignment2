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
    private final int senderFreq;

    public DetectObjectsEvent(List<DetectedObject> detectedObjects, int time, int cameraId, int senderFreq) {
        this.detectedObjects = detectedObjects;
        this.time = time;
        this.cameraId = cameraId;
        this.senderFreq = senderFreq;
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

    public int getSenderFreq() {
        return senderFreq;
    }
}
