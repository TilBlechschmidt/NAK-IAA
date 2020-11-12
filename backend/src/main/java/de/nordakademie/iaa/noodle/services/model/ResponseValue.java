package de.nordakademie.iaa.noodle.services.model;

import de.nordakademie.iaa.noodle.model.ResponseType;

/**
 * POJO with the timeslot id and response type to create a new response.
 */
public class ResponseValue {
    private final long timeslotId;
    private final ResponseType responseType;

    /**
     * Creates a new ResponseValue.
     *
     * @param timeslotId   The id of the timeslot the response is for.
     * @param responseType The type of the response.
     */
    public ResponseValue(long timeslotId, ResponseType responseType) {
        this.timeslotId = timeslotId;
        this.responseType = responseType;
    }

    public long getTimeslotId() {
        return timeslotId;
    }

    public ResponseType getResponseType() {
        return responseType;
    }
}
