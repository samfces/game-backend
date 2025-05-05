package es.samfc.gamebackend.events.producer;

import es.samfc.gamebackend.events.Event;

public interface EventProducer {

    void callEvent(Event event);

}
