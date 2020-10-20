import {DateTime, String} from "@airtasker/spot";
import {Response} from './responses/types';

/** Short title to be displayed in survey list (max. 64 characters) */
export type SurveyTitle = String;

export interface TimeSlot {
    /** Beginning of the time slot */
    start: DateTime;
    /** End of the time slot */
    end: DateTime;
}

export interface SurveyMetadata {
    title: SurveyTitle;

    /** Text describing what this survey is about (max. 1024 characters) */
    description: String;

    /** List of available time slots for the survey */
    timeSlot: TimeSlot[];
}

export interface Survey extends SurveyMetadata {
    responses: Response[];
}
