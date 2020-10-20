import {Identifier} from "../../types";

export type ResponseValue = 'yes' | 'maybe' | 'no';

export interface Response {
    /** Unique resource identifier for the user to which this response belongs */
    userID: Identifier;
    /** Unique resource identifier for the survey to which this response belongs */
    surveyID: Identifier;
    /** Response values in chronological order */
    values: ResponseValue[];
}
