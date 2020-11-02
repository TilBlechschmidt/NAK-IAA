import {DateTime, String} from "@airtasker/spot";
import {ResponseDTO} from './responses/types';
import {Identifier} from "../types";
import {IdentifiableUserDTO} from "../account/types";

/** Short title to be displayed in survey list (max. 64 characters) */
export type SurveyTitle = String;

export interface TimeslotDTO {
    id: Identifier;
    /** Beginning of the timeslot */
    start: DateTime;
    /** End of the timeslot */
    end: DateTime;
}

export interface SurveyMetadataDTO {
    title: SurveyTitle;

    /** Text describing what this survey is about (max. 1024 characters) */
    description: String;

    /** The user who created the survey. */
    creator: IdentifiableUserDTO;

    /** List of available timeslots for the survey */
    timeslots: TimeslotDTO[];

    /** Final timeslot that has been selected by the creator */
    selectedTimeslot?: TimeslotDTO;

    /** Whether the survey has been closed */
    isClosed: boolean;
}

export interface SurveyDTO extends SurveyMetadataDTO {
    responses: ResponseDTO[]
}
