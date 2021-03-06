package de.nordakademie.iaa.noodle.services.model;

import java.util.Date;
import java.util.Objects;

/**
 * POJO with the start and end date for a {@link de.nordakademie.iaa.noodle.model.Timeslot}
 * which can be used to create a new one.
 *
 * @author Noah Peeters
 */
public class TimeslotCreationData {
    private final Date start;
    private final Date end;

    /**
     * Creates new TimeslotCreationData.
     *
     * @param start The start date of the timeslot.
     * @param end   The end date of the timeslot.
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

    @Override public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        TimeslotCreationData that = (TimeslotCreationData) o;
        return Objects.equals(start, that.start) &&
               Objects.equals(end, that.end);
    }

    @Override public int hashCode() {
        return Objects.hash(start, end);
    }
}
