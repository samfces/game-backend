package es.samfc.gamebackend.events.types;

import es.samfc.gamebackend.events.Event;

public class RestEvent<T, R> extends Event {

    private RestRequestType eventType;
    private T requestData;
    private R responseData;

    protected RestEvent(RestRequestType eventType, R responseData, T requestData) {
        this.eventType = eventType;
        this.responseData = responseData;
        this.requestData = requestData;
    }

    public RestEvent() { }

    public T getRequestData() {
        return requestData;
    }

    public R getResponseData() {
        return responseData;
    }

    public void setRequestData(T requestData) {
        this.requestData = requestData;
    }

    public void setResponseData(R responseData) {
        this.responseData = responseData;
    }

    public RestRequestType getEventType() {
        return eventType;
    }

    public static class Builder<T, R> {

        private RestEvent<T, R> event = new RestEvent<>();

        public Builder<T, R> requestData(T requestData) {
            this.event.requestData = requestData;
            return this;
        }

        public Builder<T, R> responseData(R responseData) {
            this.event.responseData = responseData;
            return this;
        }

        public Builder<T, R> eventType(RestRequestType eventType) {
            this.event.eventType = eventType;
            return this;
        }

        public RestEvent<T, R> build() {
            return event;
        }
    }
}
