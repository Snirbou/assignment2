package bgu.spl.mics.application.objects;

import java.util.concurrent.atomic.AtomicInteger;

public class StatisticalFolder {

    // Singleton Holder for Thread-Safe Initialization
    private static class SingletonHolder {
        private static final StatisticalFolder instance = new StatisticalFolder();
    }

    // Metrics tracked by StatisticalFolder
    private AtomicInteger systemRuntime;
    private AtomicInteger numDetectedObjects;
    private AtomicInteger numTrackedObjects;
    private AtomicInteger numLandmarks;

    // Private constructor for singleton
    private StatisticalFolder() {
        this.systemRuntime = new AtomicInteger(0);
        this.numDetectedObjects = new AtomicInteger(0);
        this.numTrackedObjects = new AtomicInteger(0);
        this.numLandmarks = new AtomicInteger(0);
    }

    // Public access to the singleton instance
    public static StatisticalFolder getInstance() {
        return SingletonHolder.instance;
    }

    // Increments system runtime by one tick
    public void incrementRuntime() {
        systemRuntime.incrementAndGet();
    }

    // Increments the number of detected objects (from CameraService)
    public void incrementDetectedObjects() {
        numDetectedObjects.incrementAndGet();
    }

    // Increments the number of tracked objects (from LiDARService)
    public void incrementTrackedObjects() {
        numTrackedObjects.incrementAndGet();
    }

    // Updates the number of landmarks when a new landmark is added (from FusionSlamService)
    public void incrementLandmarks() {
        numLandmarks.incrementAndGet();
    }

    // Generate a report summarizing system statistics
    public String generateReport() {
        return String.format(
                "System Statistics:\n" +
                        "Runtime (ticks): %d\n" +
                        "Detected Objects: %d\n" +
                        "Tracked Objects: %d\n" +
                        "Landmarks: %d",
                systemRuntime.get(),
                numDetectedObjects.get(),
                numTrackedObjects.get(),
                numLandmarks.get()
        );
    }

    public AtomicInteger getSystemRuntime() {

        return systemRuntime;
    }

    public AtomicInteger getNumDetectedObjects() {
        return numDetectedObjects;
    }

    public AtomicInteger getNumTrackedObjects() {
        return numTrackedObjects;
    }

    public AtomicInteger getNumLandmarks() {
        return numLandmarks;
    }
}