package bgu.spl.mics.application.services;

import bgu.spl.mics.Future;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.CrashedBroadcast;
import bgu.spl.mics.application.messages.DetectObjectsEvent;
import bgu.spl.mics.application.messages.TerminatedBroadcast;
import bgu.spl.mics.application.messages.TickBroadcast;
import bgu.spl.mics.application.objects.*;

import java.util.ArrayList;
import java.util.List;

/**
 * CameraService is responsible for processing data from the camera and
 * sending DetectObjectsEvents to LiDAR workers.
 * 
 * This service interacts with the Camera object to detect objects and updates
 * the system's StatisticalFolder upon sending its observations.
 */
public class CameraService extends MicroService {
    private final Camera camera;

    /**
     * Constructor for CameraService.
     *
     * @param camera The Camera object that this service will use to detect objects.
     */
    public CameraService(Camera camera) {
        super("Camera");
        this.camera = camera;
    }

    /**
     * Initializes the CameraService.
     * Registers the service to handle TickBroadcasts and sets up callbacks for sending
     * DetectObjectsEvents.
     */
    @Override
    protected void initialize() {
        subscribeBroadcast(TickBroadcast.class, (TickBroadcast c) -> {
            int currTick = c.getTick();
            camera.retrieveCurrDetection(currTick);

            StampedDetectedObjects toSend = camera.getObjsAtTime(currTick);

            if(toSend != null){ //Camera did not get anything in this tick
                DetectObjectsEvent e = new DetectObjectsEvent(toSend.getDetectedObjects(), currTick, camera.getFrequency(), camera.getId()); //POSSIBLE TIMING BUG
//            DetectObjectsEvent e = camera.handleTick(c.getTick());
                if(e != null) {
                    Future<List<TrackedObject>> fut = sendEvent(e);
                    //if(fut.get() == false)
                    //crash?????????
                }
            }

        });
        subscribeBroadcast(TerminatedBroadcast.class, (TerminatedBroadcast c) -> {
            terminate();
            this.camera.setStatus(STATUS.DOWN);
        });

        subscribeBroadcast(CrashedBroadcast.class, (CrashedBroadcast c) -> {
           //CRASH SOMEHOW
            this.camera.setStatus(STATUS.ERROR);
        });
    }

}
