import {body, endpoint, request, response, pathParams} from "@airtasker/spot";
import {
    ConflictErrorResponseTemplate,
    Identifier,
    MalformedRequestErrorResponse, NotFoundErrorResponseTemplate,
    UnauthorizedErrorResponse,
    UnprocessableEntityErrorResponseTemplate
} from "../../types";
import {ResponseDTO, ResponseValueDTO} from "./types";

/** Responds to an existing survey as the user associated with the provided credentials */
@endpoint({
    method: "POST",
    path: "/surveys/:surveyID/responses",
    tags: ["Responses"]
})
class CreateResponse {
    @request
    request(@pathParams pathParams: CreateResponsePathParams, @body body: CreateResponseRequest) {}

    /** Response submitted */
    @response({ status: 201 })
    successfulResponse(@body body: ResponseDTO) {}

    /** Not Found */
    @response({ status: 404 })
    notFoundResponse(@body body: CreateResponseNotFoundErrorResponse) {}

    /** Duplicate response */
    @response({ status: 409 })
    duplicateResponse(@body body: CreateResponseConflictErrorResponse) {}

    /** Invalid semantics */
    @response({ status: 422 })
    semanticErrorResponse(@body body: CreateResponseUnprocessableEntityErrorResponse) {}

    // MARK: - Generic response

    /** Malformed request */
    @response({ status: 400 })
    malformedRequestResponse(@body body: MalformedRequestErrorResponse) {}

    /** Missing or invalid authentication */
    @response({ status: 401 })
    unauthorizedResponse(@body body: UnauthorizedErrorResponse) {}
}

interface CreateResponsePathParams {
    /** Unique resource identifier for the survey to respond to */
    surveyID: Identifier;
}

export interface CreateResponseRequest {
    /** Response values in chronological order */
    values: ResponseValueDTO[];
}

interface CreateResponseUnprocessableEntityErrorResponse extends UnprocessableEntityErrorResponseTemplate {
    message: "noTimeslotsSelected"
}

interface CreateResponseNotFoundErrorResponse extends NotFoundErrorResponseTemplate {
    message: "timeslotNotFound" | "surveyNotFound"
}

interface CreateResponseConflictErrorResponse extends ConflictErrorResponseTemplate {
    message: "responseExists"
}
