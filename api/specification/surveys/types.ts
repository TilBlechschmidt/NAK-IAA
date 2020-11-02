import {DateTime, String} from "@airtasker/spot";
import {Response} from './responses/types';
import {Identifier} from "../types";
import {IdentifiableUser} from "../account/types";

/** Short title to be displayed in survey list (max. 64 characters) */
export type SurveyTitle = String;

export interface Timeslot {
    id: Identifier;
    /** Beginning of the timeslot */
    start: DateTime;
    /** End of the timeslot */
    end: DateTime;
}

export interface SurveyMetadata {
    title: SurveyTitle;

    /** Text describing what this survey is about (max. 1024 characters) */
    description: String;

    /** The user who created the survey. */
    creator: IdentifiableUser;

    /** List of available timeslots for the survey */
    timeslots: Timeslot[];

    /** Final timeslot that has been selected by the creator */
    selectedTimeslot?: Timeslot;

    /** Whether the survey has been closed */
    isClosed: boolean;
}

export interface Survey extends SurveyMetadata {
    responses: Response[]
}
