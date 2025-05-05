package es.samfc.gamebackend.events;

public abstract class RestEvent<T, R> extends Event {

    private T requestData;
    private R responseData;

    protected RestEvent(String eventType, R responseData, T requestData) {
        super(eventType);
        this.responseData = responseData;
        this.requestData = requestData;
    }

    public T getRequestData() {
        return requestData;
    }

    public R getResponseData() {
        return responseData;
    }
}
