package es.samfc.gamebackend.events;

import es.samfc.gamebackend.controller.EventableController;
import es.samfc.gamebackend.events.rest.RestEvent;
import es.samfc.gamebackend.events.rest.RestEventType;

public class RestEventCall<T, R> {


    private T requestData;
    private R responseData;
    private RestEventType eventType;
    private EventableController controller;

    public RestEventCall(T requestData, R responseData, RestEventType eventType, EventableController controller) {
        this.requestData = requestData;
        this.responseData = responseData;
        this.eventType = eventType;
        this.controller = controller;
    }

    public T getRequestData() {
        return requestData;
    }

    public void setRequestData(T requestData) {
        this.requestData = requestData;
    }

    public R getResponseData() {
        return responseData;
    }

    public void setResponseData(R responseData) {
        this.responseData = responseData;
    }

    public RestEventType getEventType() {
        return eventType;
    }

    public void setEventType(RestEventType eventType) {
        this.eventType = eventType;
    }

    public EventableController getController() {
        return controller;
    }

    public void setController(EventableController controller) {
        this.controller = controller;
    }

    public void callEvent() {
        getController().getEventProducer().callEvent(new RestEvent.Builder<>()
                .eventType(eventType)
                .requestData(requestData)
                .responseData(responseData)
                .build()
        );
    }

    public static class Builder<T, R> {

        private RestEventType eventType;
        private EventableController controller;
        private T requestData = null;
        private R responseData = null;

        public Builder<T, R> requestData(T requestData) {
            this.requestData = requestData;
            return this;
        }

        public Builder<T, R> responseData(R responseData) {
            this.responseData = responseData;
            return this;
        }

        public Builder<T, R> eventType(RestEventType eventType) {
            this.eventType = eventType;
            return this;
        }

        public Builder<T, R> controller(EventableController controller) {
            this.controller = controller;
            return this;
        }

        public RestEventCall<T, R> build() {
            return new RestEventCall<>(requestData, responseData, eventType, controller);
        }
    }
}
