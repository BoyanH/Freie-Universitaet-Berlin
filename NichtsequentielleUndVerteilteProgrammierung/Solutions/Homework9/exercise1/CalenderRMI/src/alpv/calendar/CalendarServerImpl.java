package alpv.calendar;

import java.lang.reflect.Array;
import java.rmi.RemoteException;
import java.util.*;

public class CalendarServerImpl implements CalendarServer {

    private Dictionary<Long, Event> eventsById;
    private Dictionary<Long, EventWaiterThread> waiterForEvent;
    private Object eventsLock;
    private static long eventsCount = 0;
    private Dictionary<String, List<EventCallback>> callbacksForUser;

    public CalendarServerImpl() {
        this.eventsById = new Hashtable<>();
        this.waiterForEvent = new Hashtable<>();
        this.eventsLock = new Object();
        this.callbacksForUser = new Hashtable<>();
    }

    @Override
    public long addEvent(Event e) throws RemoteException {
        e.setId(++eventsCount);

        synchronized (eventsLock) {
            long id = e.getId();
            EventWaiterThread eventWaiter = new EventWaiterThread(e, this);
            this.eventsById.put(id, e);
            this.waiterForEvent.put(id, eventWaiter);
            eventWaiter.start();
            return id;
        }
    }

    @Override
    public boolean removeEvent(long id) throws RemoteException {
        synchronized (eventsLock) {
            Event registeredEventWithId = this.eventsById.remove(id);
            EventWaiterThread waiterForEvent = this.waiterForEvent.remove(id);

            if (waiterForEvent != null) {
                waiterForEvent.interrupt();
            }

            return registeredEventWithId != null;
        }
    }

    @Override
    public boolean updateEvent(long id, Event e) throws RemoteException {
        synchronized (eventsLock) {
            Event registeredEventWithId = this.eventsById.get(id);
            EventWaiterThread waiterForEvent = this.waiterForEvent.get(id);

            /**
             * If the date has changed, we need to restart the waiter thread as well, so just
             * add a new event with the same id, remove the current one
             *
             * Not the best implementation ever, but keeps our timeout logic clean and simple
             */
            if (registeredEventWithId == null) {
                return false;
            } else if (registeredEventWithId.getBegin().compareTo(e.getBegin()) == 0) {

                registeredEventWithId.setUser(e.getUser());
                registeredEventWithId.setName(e.getName());

            } else {
                e.setId(registeredEventWithId.getId());
                this.removeEvent(id);
                this.addEvent(e);
                return true;
            }
        }

        return false;
    }

    @Override
    public List<Event> listEvents(String user) throws RemoteException {
        synchronized (eventsLock) {
            List<Event> eventsList = new LinkedList<>();
            Enumeration<Event> events = this.eventsById.elements();
            Event currentEvent = null;

            try {
                do {
                    currentEvent = events.nextElement();

                    if (currentEvent != null && Arrays.asList(currentEvent.getUser()).indexOf(user) > -1) {
                        eventsList.add(currentEvent);
                    }
                }
                while (currentEvent != null);
            } catch (NoSuchElementException e) {
                // dictionary has no further items
            }


            return eventsList;
        }
    }

    @Override
    public Event getNextEvent(String user) throws RemoteException {
        Event closestEvent;
        long timeBeforeNextEvent;
        Date currentDate = new Date();

        synchronized (eventsLock) {
            List<Event> userEvents = this.listEvents(user);
            closestEvent = userEvents.get(0);

            for (Event currentEvent : userEvents) {
                // if the event is closer in the future, but still after the current date
                if (currentEvent.compareTo(closestEvent) < 0 && currentDate.compareTo(currentDate) < 0) {
                    closestEvent = currentEvent;
                }
            }
        }

        if (closestEvent != null) {
            timeBeforeNextEvent = closestEvent.getBegin().getTime() - new Date().getTime();
            try {
                Thread.sleep(timeBeforeNextEvent);
                return closestEvent;
            } catch (InterruptedException e) {
                // interrupted, don't return anything
                return null;
            }
        }

        return null;
    }

    @Override
    public void RegisterCallback(EventCallback ec, String user) throws RemoteException {
        List<EventCallback> callbacksForGivenUser = this.callbacksForUser.get(user);

        if (callbacksForGivenUser == null) {
            List newList = new LinkedList<>();
            newList.add(ec);
            this.callbacksForUser.put(user, newList);
        } else {
            callbacksForGivenUser.add(ec);
        }
    }

    /**
     * Why the fuck is the user not given here but was given in RegisterCallback?
     * ...consistency is for the weak...no worries, I'll find it from all callbacks...
     */
    @Override
    public void UnregisterCallback(EventCallback ec) throws RemoteException {
        Enumeration<List<EventCallback>> callbacks = this.callbacksForUser.elements();
        List<EventCallback> currentUserCallbacks;

        try {
            do {
                currentUserCallbacks = callbacks.nextElement();

                // if the callback was found in the current list of callbacks, stop searching
                // it was successfully removed
                if (currentUserCallbacks != null && currentUserCallbacks.remove(ec)) {
                    break;
                }

            } while (currentUserCallbacks != null);
        } catch(NoSuchElementException e) {
            // dictionary has no further elements
        }

    }

    public void eventStarted(long id) {
        Event startedEvent = this.eventsById.get(id);
        String[] users = startedEvent.getUser();

        for (String user : users) {

            for (EventCallback ec : this.callbacksForUser.get(user)) {
                try {
                    ec.call(startedEvent);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
