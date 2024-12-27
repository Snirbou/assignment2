package bgu.spl.mics.application.services;

import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.CrashedBroadcast;
import bgu.spl.mics.application.messages.DetectObjectsEvent;
import bgu.spl.mics.application.messages.TerminatedBroadcast;
import bgu.spl.mics.application.messages.TickBroadcast;
import bgu.spl.mics.application.objects.StatisticalFolder;

import java.util.List;

/**
 * TimeService acts as the global timer for the system, broadcasting TickBroadcast messages
 * at regular intervals and controlling the simulation's duration.
 */
public class TimeService extends MicroService {

    private int tickTime;
    private int duration;

    /**
     * Constructor for TimeService.
     *
     * @param TickTime  The duration of each tick in milliseconds.
     * @param Duration  The total number of ticks before the service terminates.
     */
    public TimeService(int TickTime, int Duration) {
        super("Time");
        this.tickTime = TickTime;
        this.duration = Duration;
    }

    /**
     * Initializes the TimeService.
     * Starts broadcasting TickBroadcast messages and terminates after the specified duration.
     */
    @Override
    protected void initialize() {

        Thread t = new Thread(() -> {
            for (int i = 1; i <= duration; i++) {
                sendBroadcast(new TickBroadcast(i));  // שלח טיק חדש עם מספר הטיק הנוכחי
                StatisticalFolder.getInstance().incrementRuntime();
                try {
                    Thread.sleep(tickTime);  // השהייה לפי זמן הטיק
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();  // אם הופרע, סיים את הלולאה
                    sendBroadcast(new CrashedBroadcast("TimeService"));
                    break;
                }
            }
            sendBroadcast(new TerminatedBroadcast("TimeService"));  // שדר סיום לכל השירותים
            terminate();  // סיים את TimeService
        });
        //t.start();

        subscribeBroadcast(TerminatedBroadcast.class, (TerminatedBroadcast c) -> {
            terminate();
        });

        //ADD CRASHED METHOD
    }
}
