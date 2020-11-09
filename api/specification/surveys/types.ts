import {DateTime, String} from "@airtasker/spot";
import {ResponseDTO} from './responses/types';
import {Identifier} from "../types";
import {IdentifiableUserDTO} from "../account/types";

/** Short title to be displayed in survey list (max. 64 characters) */
export type SurveyTitle = String;

export interface TimeslotCreationDTO {
    /** Beginning of the timeslot */
    start: DateTime;
    /** End of the timeslot */
    end: DateTime;
}

export interface TimeslotDTO extends TimeslotCreationDTO {
    id: Identifier;
}

export interface SurveyGeneralMetadataDTO {
    title: SurveyTitle;

    /** Text describing what this survey is about (max. 1024 characters) */
    description: String;
}

export interface SurveyCreationMetadataDTO extends SurveyGeneralMetadataDTO {
    /** List of available timeslots for the survey */
    timeslots: TimeslotCreationDTO[];
}

export interface SurveyMetadataDTO extends SurveyGeneralMetadataDTO{
    id: Identifier;

    /** The user who created the survey. */
    creator: IdentifiableUserDTO;

    /** List of available timeslots for the survey */
    timeslots: TimeslotDTO[];

    /** Final timeslot that has been selected by the creator */
    selectedTimeslot?: TimeslotDTO;

    /** Whether the survey has been closed */
    isClosed: boolean;

    /** The current user can delete the survey */
    isDeletable: boolean;

    /** The current user can close the survey */
    isClosable: boolean;

    /** The current user can edit the survey */
    isEditable: boolean;
}

export interface SurveyDTO extends SurveyMetadataDTO {
    responses: ResponseDTO[]
    myResponse?: ResponseDTO
}
