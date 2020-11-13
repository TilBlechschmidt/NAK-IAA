package de.nordakademie.iaa.noodle.dao.model;

import de.nordakademie.iaa.noodle.model.Timeslot;

/**
 * Survey data subset with the data required for listing surveys.
 *
 * @author Hans Rißer
 * @see de.nordakademie.iaa.noodle.model.Survey
 */
public interface QuerySurveysItem {
    Long getId();

    String getTitle();

    Long getResponseCount();

    Timeslot getSelectedTimeslot();
}
