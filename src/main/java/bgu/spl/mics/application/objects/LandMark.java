package bgu.spl.mics.application.objects;

import java.util.ArrayList;

/**
 * Represents a landmark in the environment map.
 * Landmarks are identified and updated by the FusionSlam service.
 */
public class LandMark {
    private String Id;
    private String Description;
    private ArrayList<CloudPoint> Coordinates;

    public LandMark(String id, String description, ArrayList<CloudPoint> coordinates) {
        Id = id;
        Description = description;
        Coordinates = coordinates;
    }

    public String getId() {
        return Id;
    }

    public String getDescription() {
        return Description;
    }

    public ArrayList<CloudPoint> getCoordinates() {
        return Coordinates;
    }

    public synchronized void setCoordinates(ArrayList<CloudPoint> coordinates) {
        Coordinates = coordinates;
    }
}
