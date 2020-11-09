package de.nordakademie.iaa.noodle.services.model;

import java.util.Date;

/**
 * POJO with the start and end Date for a Timeslot which can be used to create a new one.
 */
public class TimeslotCreationData {
    private final Date start;
    private final Date end;

    /**
     * Creates new TimeslotCreationData.
     * @param start The start date of the timeslot.
     * @param end The end date of the timeslot.
     */
    public TimeslotCreationData(Date start, Date end) {
        this.start = start;
        this.end = end;
    }

    public Date getStart() {
        return start;
    }

    public Date getEnd() {
        return end;
    }
}
