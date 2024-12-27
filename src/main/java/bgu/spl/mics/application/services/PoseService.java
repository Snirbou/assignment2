package bgu.spl.mics.application.services;

import bgu.spl.mics.Future;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.PoseEvent;
import bgu.spl.mics.application.messages.TerminatedBroadcast;
import bgu.spl.mics.application.messages.TickBroadcast;
import bgu.spl.mics.application.messages.TrackedObjectsEvent;
import bgu.spl.mics.application.objects.GPSIMU;
import bgu.spl.mics.application.objects.Pose;
import bgu.spl.mics.application.objects.STATUS;

/**
 * PoseService is responsible for maintaining the robot's current pose (position and orientation)
 * and broadcasting PoseEvents at every tick.
 */
public class PoseService extends MicroService {

    private final GPSIMU gpsimu;

    /**
     * Constructor for PoseService.
     *
     * @param gpsimu The GPSIMU object that provides the robot's pose data.
     */
    public PoseService(GPSIMU gpsimu) {
        super("Pose");
        this.gpsimu = gpsimu;
    }

    /**
     * Initializes the PoseService.
     * Subscribes to TickBroadcast and sends PoseEvents at every tick based on the current pose.
     */
    @Override
    protected void initialize() {

        subscribeBroadcast(TickBroadcast.class, (TickBroadcast c) -> {
            this.gpsimu.incrementTick();
            Pose currPose = gpsimu.getPoseList().get(gpsimu.getCurrentTick());
            sendEvent(new PoseEvent(currPose));
        });

        subscribeBroadcast(TerminatedBroadcast.class, (TerminatedBroadcast c) -> {
            terminate();
            this.gpsimu.setStatus(STATUS.DOWN);
        });    }
}
