package bgu.spl.mics.application.objects;
import java.util.ArrayList;

/**
 * Represents a group of cloud points corresponding to a specific timestamp.
 * Used by the LiDAR system to store and process point cloud data for tracked objects.
 */
public class StampedCloudPoints {

    private String ID;
    private int time;
    private ArrayList<CloudPoint> cloudPoints;

    public StampedCloudPoints(String ID, int time, ArrayList<CloudPoint> cloudPoints) {
        this.ID = ID;
        this.time = time;
        this.cloudPoints = cloudPoints;
    }

    public String getID() {
        return ID;
    }

    public int getTime() {
        return time;
    }

    public ArrayList<CloudPoint> getCloudPoints() {
        return cloudPoints;
    }


}
