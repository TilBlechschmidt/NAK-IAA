package de.nordakademie.iaa.noodle.dao.model;

/**
 * Survey data subset with the data required for listing surveys.
 */
public interface QuerySurveysItem {
    Long getId();

    String getTitle();

    Long getResponseCount();
}
