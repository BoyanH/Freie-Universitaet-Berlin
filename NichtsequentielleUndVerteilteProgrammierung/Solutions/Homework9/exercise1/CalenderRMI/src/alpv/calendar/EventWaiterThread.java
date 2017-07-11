package alpv.calendar;

import java.util.Date;

public class EventWaiterThread extends Thread {

    private Event event;
    private CalendarServerImpl server;

    public EventWaiterThread(Event e, CalendarServerImpl server) {
        this.event = e;
        this.server = server;
    }

    @Override
    public void run() {
        Date currentDate = new Date();
        System.out.println(this.event.getBegin());
        long millisecondsToBegin = this.event.getBegin().getTime() - currentDate.getTime();
        try {
            EventWaiterThread.sleep(millisecondsToBegin);
            this.server.eventStarted(this.event.getId());
        } catch (InterruptedException e) {
            // interrupted, just end
            // e.printStackTrace();
        }

    }
}
