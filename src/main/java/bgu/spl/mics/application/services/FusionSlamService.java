package bgu.spl.mics.application.services;

import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.PoseEvent;
import bgu.spl.mics.application.messages.TerminatedBroadcast;
import bgu.spl.mics.application.messages.TickBroadcast;
import bgu.spl.mics.application.messages.TrackedObjectsEvent;
import bgu.spl.mics.application.objects.CloudPoint;
import bgu.spl.mics.application.objects.FusionSlam;
import bgu.spl.mics.application.objects.LandMark;
import bgu.spl.mics.application.objects.TrackedObject;

import java.util.ArrayList;


/**
 * FusionSlamService integrates data from multiple sensors to build and update
 * the robot's global map.
 * 
 * This service receives TrackedObjectsEvents from LiDAR workers and PoseEvents from the PoseService,
 * transforming and updating the map with new landmarks.
 */




public class FusionSlamService extends MicroService {
    /**
     * Constructor for FusionSlamService.
     *
     * @param fusionSlam The FusionSLAM object responsible for managing the global map.
     */

    private final FusionSlam fusion;

    public FusionSlamService(FusionSlam fusionSlam) {
        super("Fusion Slam");
        this.fusion = FusionSlam.getInstance();
    }

    /**
     * Initializes the FusionSlamService.
     * Registers the service to handle TrackedObjectsEvents, PoseEvents, and TickBroadcasts,
     * and sets up callbacks for updating the global map.
     */
    @Override
    protected void initialize() {
        subscribeEvent(TrackedObjectsEvent.class, (TrackedObjectsEvent trackedObj) -> {
            ArrayList<TrackedObject> currTracked = trackedObj.getTrackedObjects();
            for(TrackedObject tracked : currTracked)
            {
                boolean flag = false;
                for(LandMark landmark : fusion.getLandmark())
                {
                    if(!flag && landmark.getId() == tracked.getId())
                    {
                        flag = true;
                        ArrayList<CloudPoint> makeAvg = new ArrayList<>();
                        int INDEX;
                        for (INDEX = 0; INDEX < landmark.getCoordinates().size(); INDEX++)
                        {
                            double avgX = (tracked.getCoordinates().get(INDEX).getX() + landmark.getCoordinates().get(INDEX).getX()) / 2;
                            double avgY = (tracked.getCoordinates().get(INDEX).getY() + landmark.getCoordinates().get(INDEX).getY()) / 2;
                            makeAvg.add(new CloudPoint(avgX, avgY));
                        }

                        for(; INDEX < tracked.getCoordinates().size(); INDEX++) //ADD REMAINDER IN CASE ITS LONGER
                            makeAvg.add(new CloudPoint(tracked.getCoordinates().get(INDEX).getX(), tracked.getCoordinates().get(INDEX).getY()));
                        landmark.setCoordinates(makeAvg);
                    }
                }
                if(!flag)
                {
                    LandMark copied = new LandMark(tracked.getId(), tracked.getDescription(), tracked.getCoordinates());
                    this.fusion.getLandmark().add(copied);
                }
            }

        });

        subscribeEvent(PoseEvent.class, (PoseEvent pose) -> {
            this.fusion.getPoses().add(pose.getCurrentPose());
        });

        subscribeBroadcast(TerminatedBroadcast.class, (TerminatedBroadcast c) -> {
            terminate();
        });    }
}
