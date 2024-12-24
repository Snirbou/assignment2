package bgu.spl.mics.application.messages;

import bgu.spl.mics.application.objects.Pose;
import bgu.spl.mics.Event;
public class PoseEvent implements Event<Boolean> {

    private Pose currentPose;

    public PoseEvent(Pose currentPose)
    {
        this.currentPose = currentPose;
    }

    public Pose getCurrentPose(){
        return currentPose;
    }

    public String toString()
    {
        return currentPose.toString();
    }

}
