package bgu.spl.mics.application.objects;

/**
 * Holds statistical information about the system's operation.
 * This class aggregates metrics such as the runtime of the system,
 * the number of objects detected and tracked, and the number of landmarks identified.
 */
public class StatisticalFolder {

    private int systemRuntime;
    private int numDetectedObjects;
    private int numTrackedObject;
    private int numLandmarks;

    private

    public StatisticalFolder(int systemRuntime, int numDetectedObjects, int numTrackedObject, int numLandmarks) {
        this.systemRuntime = systemRuntime;
        this.numDetectedObjects = numDetectedObjects;
        this.numTrackedObject = numTrackedObject;
        this.numLandmarks = numLandmarks;
    }

    public int getSystemRuntime() {
        return systemRuntime;
    }

    public synchronized void setSystemRuntime(int systemRuntime) {
        this.systemRuntime = systemRuntime;
    }

    public int getNumDetectedObjects() {
        return numDetectedObjects;
    }

    public synchronized void setNumDetectedObjects(int numDetectedObjects) {
        this.numDetectedObjects = numDetectedObjects;
    }

    public int getNumTrackedObject() {
        return numTrackedObject;
    }

    public synchronized void setNumTrackedObject(int numTrackedObject) {
        this.numTrackedObject = numTrackedObject;
    }

    public int getNumLandmarks() {
        return numLandmarks;
    }

    public synchronized void setNumLandmarks(int numLandmarks) {
        this.numLandmarks = numLandmarks;
    }
}
