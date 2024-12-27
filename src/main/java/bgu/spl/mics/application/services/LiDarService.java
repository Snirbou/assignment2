package bgu.spl.mics.application.services;

import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.DetectObjectsEvent;
import bgu.spl.mics.application.messages.TerminatedBroadcast;
import bgu.spl.mics.application.messages.TickBroadcast;
import bgu.spl.mics.application.messages.TrackedObjectsEvent;
import bgu.spl.mics.application.objects.*;

import java.util.ArrayList;
import java.util.List;

/**
 * LiDarService is responsible for processing data from the LiDAR sensor and
 * sending TrackedObjectsEvents to the FusionSLAM service.
 * 
 * This service interacts with the LiDarWorkerTracker object to retrieve and process
 * cloud point data and updates the system's StatisticalFolder upon sending its
 * observations.
 */
public class LiDarService extends MicroService {

    private LiDarWorkerTracker tracker;

    /**
     * Constructor for LiDarService.
     *
     * @param LiDarWorkerTracker A LiDAR Tracker worker object that this service will use to process data.
     */
    public LiDarService(LiDarWorkerTracker LiDarWorkerTracker) {
        super("LiDarWorker");
        this.tracker = LiDarWorkerTracker;
    }

    /**
     * Initializes the LiDarService.
     * Registers the service to handle DetectObjectsEvents and TickBroadcasts,
     * and sets up the necessary callbacks for processing data.
     */
    @Override
    protected void initialize() {
        subscribeBroadcast(TickBroadcast.class, (TickBroadcast c) -> {

            int currTick = c.getTick();
            ArrayList<TrackedObject> toSend = new ArrayList<>();
            for(TrackedObject trackedObj : tracker.getLastTrackedObjects())
            {
                if(trackedObj.getTime() == currTick)
                {
                    toSend.add(trackedObj);
                    StatisticalFolder.getInstance().incrementTrackedObjects();
                    //MAYBE DELETE FROM TRACKER
                }
            }


            if(!toSend.isEmpty())
            {
                TrackedObjectsEvent currTrackedEvent = new TrackedObjectsEvent(toSend, currTick, tracker.getId());
                sendEvent(currTrackedEvent);
            }

        });
        subscribeEvent(DetectObjectsEvent.class, (DetectObjectsEvent detected) -> {

            int detectTime = detected.getTime() - detected.getSenderFreq();
            ArrayList<StampedCloudPoints> cloudPoints = LiDarDataBase.getInstance().getCloudPointsByTime(detectTime);
            for (DetectedObject obj : detected.getDetectedObjects())
            {
                String objID = obj.getId();
                String objDesc = obj.getDescription();
                for(StampedCloudPoints cloudPoint : cloudPoints)
                {
                    if(objID.equals(cloudPoint.getID()))
                        tracker.getLastTrackedObjects().add((new TrackedObject(objID, detectTime + tracker.getFrequency(), objDesc, cloudPoint.getCloudPoints())));
                }
            }



        });
        subscribeBroadcast(TerminatedBroadcast.class, (TerminatedBroadcast c) -> {
            terminate();
            tracker.SetStatus(STATUS.DOWN);
        });
    }
}
