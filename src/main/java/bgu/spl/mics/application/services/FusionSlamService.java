package bgu.spl.mics.application.services;

import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.PoseEvent;
import bgu.spl.mics.application.messages.TerminatedBroadcast;
import bgu.spl.mics.application.messages.TickBroadcast;
import bgu.spl.mics.application.messages.TrackedObjectsEvent;
import bgu.spl.mics.application.objects.*;

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
    public Pose currentPose;

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
                LandMark transformed = fusion.translateCoordinateSys(tracked, this.currentPose);
                boolean flag = false;
                for(LandMark landmark : fusion.getLandmark())
                {
                    if(!flag && landmark.getId().equals(transformed.getId()))
                    {
                        flag = true;
                        ArrayList<CloudPoint> makeAvg = fusion.averageCoordinates(landmark.getCoordinates(), tracked.getCoordinates());
                        landmark.setCoordinates(makeAvg);
                    }
                }
                if(!flag)
                {
                    LandMark copied = new LandMark(tracked.getId(), tracked.getDescription(), tracked.getCoordinates());
                    this.fusion.getLandmark().add(copied);
                    StatisticalFolder.getInstance().incrementLandmarks();
                }
            }

        });

        subscribeEvent(PoseEvent.class, (PoseEvent pose) -> {
            this.currentPose = pose.getCurrentPose();
        });

        subscribeBroadcast(TerminatedBroadcast.class, (TerminatedBroadcast c) -> {
            terminate();
        });    }

    public FusionSlam getFusion() {
        return fusion;
    }
}
