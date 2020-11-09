package de.nordakademie.iaa.noodle.dao.model;

/**
 * Survey data subset with the data required for listing surveys.
 * @see de.nordakademie.iaa.noodle.model.Survey
 */
public interface QuerySurveysItem {
    Long getId();

    String getTitle();

    Long getResponseCount();
}
