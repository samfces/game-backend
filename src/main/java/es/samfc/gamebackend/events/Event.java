package es.samfc.gamebackend.events;

import java.util.Date;

public class Event {

    private String eventType;
    private final Date timestamp = new Date();

    protected Event(String eventType) {
        this.eventType = eventType;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public String getEventType() {
        return eventType;
    }


}
