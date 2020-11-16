import {body, endpoint, request, response} from "@airtasker/spot";
import {
    Identifier,
    MalformedRequestErrorResponse,
    UnauthorizedErrorResponse,
    UnprocessableEntityErrorResponseTemplate
} from "../types";
import {SurveyCreationMetadataDTO, SurveyMetadataDTO} from "./types";

/*
 * @author Til Blechschmidt
 */

/** Creates a new survey for other users to participate */
@endpoint({
    method: "POST",
    path: "/surveys",
    tags: ["Surveys"]
})
class CreateSurvey {
    @request
    request(@body body: SurveyCreationMetadataDTO) {}

    /** Survey created */
    @response({ status: 201 })
    successfulResponse(@body body: SurveyMetadataDTO) {}

    /** Invalid semantics */
    @response({ status: 422 })
    semanticErrorResponse(@body body: CreateSurveyUnprocessableEntityErrorResponse) {}

    // MARK: - Generic response

    /** Malformed request */
    @response({ status: 400 })
    malformedRequestResponse(@body body: MalformedRequestErrorResponse) {}

    /** Missing or invalid authentication */
    @response({ status: 401 })
    unauthorizedResponse(@body body: UnauthorizedErrorResponse) {}
}

interface CreateSurveyUnprocessableEntityErrorResponse extends UnprocessableEntityErrorResponseTemplate {
    message: "noTimeslots" | "emptyTitle" | "titleTooLong" | "descriptionTooLong" | "invalidTimeslot"
}
