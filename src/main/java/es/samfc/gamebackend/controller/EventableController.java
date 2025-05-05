package es.samfc.gamebackend.controller;

import es.samfc.gamebackend.events.producer.EventProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EventableController {

    @Autowired private EventProducer eventProducer;

    public EventProducer getEventProducer() {
        return eventProducer;
    }
}
