import {body, endpoint, request, response, pathParams} from "@airtasker/spot";
import {
    ConflictErrorResponseTemplate, ForbiddenErrorResponseTemplate,
    Identifier,
    MalformedRequestErrorResponse,
    NotFoundErrorResponseTemplate,
    UnauthorizedErrorResponse, UnprocessableEntityErrorResponseTemplate
} from "../../types";
import {ResponseDTO} from "./types";
import {CreateResponseRequest} from "./create";

/** Updates an existing response */
@endpoint({
    method: "PATCH",
    path: "/surveys/:surveyID/responses/:responseID",
    tags: ["Responses"]
})
class UpdateResponse {
    @request
    request(@pathParams pathParams: UpdateResponsePathParams, @body body: CreateResponseRequest) {}

    /** Response submitted */
    @response({ status: 201 })
    successfulResponse(@body body: ResponseDTO) {}

    /** Not Found */
    @response({ status: 404 })
    notFoundResponse(@body body: UpdateResponseNotFoundErrorResponse) {}

    /** Not Editable */
    @response({ status: 403 })
    forbiddenResponse(@body body: UpdateResponseForbiddenErrorResponse) {}

    /** Invalid semantics */
    @response({ status: 422 })
    semanticErrorResponse(@body body: UpdateResponseUnprocessableEntityErrorResponse) {}

    // MARK: - Generic response

    /** Malformed request */
    @response({ status: 400 })
    malformedRequestResponse(@body body: MalformedRequestErrorResponse) {}

    /** Missing or invalid authentication */
    @response({ status: 401 })
    unauthorizedResponse(@body body: UnauthorizedErrorResponse) {}
}

interface UpdateResponsePathParams {
    /** Unique resource identifier for the survey */
    surveyID: Identifier;
    /** Unique resource identifier for the response */
    responseID: Identifier;
}

interface UpdateResponseUnprocessableEntityErrorResponse extends UnprocessableEntityErrorResponseTemplate {
    message: "noTimeslotsSelected"
}

interface UpdateResponseNotFoundErrorResponse extends NotFoundErrorResponseTemplate {
    message: "responseNotFound" | "timeslotNotFound"
}

interface UpdateResponseForbiddenErrorResponse extends ForbiddenErrorResponseTemplate {
    message: "notEditable"
}
