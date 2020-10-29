import {Identifier} from "../../types";
import {IdentifiableUser} from "../../users/types";

export interface ResponseValue {
    timeslotID: Identifier;
    value: boolean;
}

export interface Response {
    /** Whether the authenticated user can modify this response */
    isEditable: boolean;
    /** Resource object of the user that created the response */
    user: IdentifiableUser;
    /** Unique resource identifier for the survey to which this response belongs */
    surveyID: Identifier;
    /**
     * List of responses
     * @type {Object.<Identifier, Timeslot>}
     */
    responses: ResponseValue[];
}
