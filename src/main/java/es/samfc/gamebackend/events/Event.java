package es.samfc.gamebackend.events;

import java.util.Date;

public class Event {

    private final Date timestamp = new Date();

    public Date getTimestamp() {
        return timestamp;
    }

}
