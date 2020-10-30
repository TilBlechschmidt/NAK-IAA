import {body, endpoint, request, response} from "@airtasker/spot";
import {Identifier, MalformedRequestErrorResponse, UnauthorizedErrorResponse} from "../types";
import {SurveyMetadata} from "./types";

/** Creates a new survey for other users to participate */
@endpoint({
    method: "POST",
    path: "/surveys",
    tags: ["Surveys"]
})
class CreateSurvey {
    @request
    request(@body body: CreateSurveyRequest) {}

    /** Survey created */
    @response({ status: 201 })
    successfulResponse(@body body: CreateSurveyResponse) {}

    /** Invalid semantics */
    @response({ status: 422 })
    semanticErrorResponse(@body body: CreateSurveyErrorResponse) {}

    // MARK: - Generic response

    /** Malformed request */
    @response({ status: 400 })
    malformedRequestResponse(@body body: MalformedRequestErrorResponse) {}

    /** Missing or invalid authentication */
    @response({ status: 401 })
    unauthorizedResponse(@body body: UnauthorizedErrorResponse) {}
}

interface CreateSurveyRequest extends SurveyMetadata {}

interface CreateSurveyResponse extends SurveyMetadata {
    id: Identifier;
}

export interface CreateSurveyErrorResponse {
    code: "invalidTimeslot" | "missingTitle" | "titleTooLong" | "descriptionTooLong" | "atLeastOneTimeslotRequired";
}
