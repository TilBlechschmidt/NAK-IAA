import {Identifier} from "../../types";
import {IdentifiableUserDTO} from "../../account/types";

/*
 * @author Til Blechschmidt
 */

export interface ResponseValueDTO {
    timeslotID: Identifier;
    value: boolean;
}

export interface ResponseDTO {
    /** Whether the authenticated user can modify this response */
    isEditable: boolean;
    /** Resource object of the user that created the response */
    user: IdentifiableUserDTO;
    /** Unique resource identifier for the survey to which this response belongs */
    surveyID: Identifier;
    /** Unique resource identifier for the response */
    responseID: Identifier;
    /**
     * List of responses
     * @type {Object.<Identifier, TimeslotDTO>}
     */
    responses: ResponseValueDTO[];
}
