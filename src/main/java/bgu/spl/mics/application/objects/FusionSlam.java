package bgu.spl.mics.application.objects;

import java.util.ArrayList;
import java.util.List;

/**
 * Manages the fusion of sensor data for simultaneous localization and mapping (SLAM).
 * Combines data from multiple sensors (e.g., LiDAR, camera) to build and update a global map.
 * Implements the Singleton pattern to ensure a single instance of FusionSlam exists.
 */
public class FusionSlam {

    // Thread-safe Singleton with Lazy Initialization
    private static class SingletonHolder {
        private static final FusionSlam INSTANCE = new FusionSlam();
    }

    private ArrayList<LandMark> landmark;
    private ArrayList<Pose> poses;

    // Private constructor to prevent instantiation
    private FusionSlam() {
        landmark = new ArrayList<>();
        poses = new ArrayList<>();
    }

    // Public access point to get the singleton instance
    public static FusionSlam getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public ArrayList<LandMark> getLandmark() {
        return landmark;
    }

    public ArrayList<Pose> getPoses() {
        return poses;
    }

    // Translate tracked object coordinates to the global frame using the current pose
    public LandMark translateCoordinateSys(TrackedObject trackedObject, Pose currentPose) {
        ArrayList<CloudPoint> globalCoordinates = new ArrayList<>();
        double yaw_rad = currentPose.getYaw() * Math.PI / 180;
        for (CloudPoint localPoint : trackedObject.getCoordinates()) {
            double x_global = localPoint.getX() * Math.cos(yaw_rad) - localPoint.getY() * Math.sin(yaw_rad) + currentPose.getX();
            double y_global = localPoint.getX() * Math.sin(yaw_rad) + localPoint.getY() * Math.cos(yaw_rad) + currentPose.getY();
            globalCoordinates.add(new CloudPoint(x_global, y_global));
        }
        return new LandMark(trackedObject.getId(), trackedObject.getDescription(), globalCoordinates);
    }

    // Average coordinates between existing landmarks and incoming tracked points
    public ArrayList<CloudPoint> averageCoordinates(List<CloudPoint> existing, List<CloudPoint> incoming) {
        int INDEX;
        ArrayList<CloudPoint> toReturn = new ArrayList<>();
        for (INDEX = 0; INDEX < existing.size(); INDEX++) {
            double avgX = (incoming.get(INDEX).getX() + existing.get(INDEX).getX()) / 2;
            double avgY = (incoming.get(INDEX).getY() + existing.get(INDEX).getY()) / 2;
            toReturn.add(new CloudPoint(avgX, avgY));
        }

        // Add any remaining points from incoming list
        for (; INDEX < incoming.size(); INDEX++) {
            toReturn.add(new CloudPoint(incoming.get(INDEX).getX(), incoming.get(INDEX).getY()));
        }

        return toReturn;
    }

    public void setPoses(ArrayList<Pose> poses) {
        this.poses = poses;
    }
}

