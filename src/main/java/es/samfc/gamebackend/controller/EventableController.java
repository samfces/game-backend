package es.samfc.gamebackend.controller;

import es.samfc.gamebackend.events.producer.EventProducer;
import es.samfc.gamebackend.events.rest.RestEvent;
import es.samfc.gamebackend.events.rest.RestEventType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EventableController {

    @Autowired private EventProducer eventProducer;

    public EventProducer getEventProducer() {
        return eventProducer;
    }

    public void callEvent(RestEventType type, Object requestData, Object responseData) {
        getEventProducer().callEvent(new RestEvent.Builder<>()
                .eventType(type)
                .requestData(requestData)
                .responseData(responseData)
                .build()
        );
    }
}
