import {body, endpoint, pathParams, request, response} from "@airtasker/spot";
import {
    Identifier,
    MalformedRequestErrorResponse,
    UnauthorizedErrorResponse,
    UnprocessableEntityErrorResponseTemplate,
    NotFoundErrorResponseTemplate, ForbiddenErrorResponseTemplate
} from "../types";
import {SurveyCreationMetadataDTO, SurveyMetadataDTO} from "./types";

/*
 * @author Til Blechschmidt
 */

/** Updates metadata of survey <strong>and deletes all responses</strong> */
@endpoint({
    method: "PUT",
    path: "/surveys/:id",
    tags: ["Surveys"]
})
class UpdateSurvey {
    @request
    request(@pathParams pathParams: { id: Identifier }, @body body: SurveyCreationMetadataDTO) {}

    /** Survey updated */
    @response({ status: 200 })
    successfulResponse(@body body: SurveyMetadataDTO) {}

    /** Invalid semantics */
    @response({ status: 422 })
    semanticErrorResponse(@body body: UpdateSurveyUnprocessableEntityErrorResponse) {}

    /** Not Editable */
    @response({ status: 403 })
    forbiddenResponse(@body body: UpdateSurveyForbiddenErrorResponse) {}

    // MARK: - Generic response

    /** Resource not found */
    @response({ status: 404 })
    notFoundResponse(@body body: UpdateSurveyNotFoundErrorResponse) {}

    /** Malformed request */
    @response({ status: 400 })
    malformedRequestResponse(@body body: MalformedRequestErrorResponse) {}

    /** Missing or invalid authentication */
    @response({ status: 401 })
    unauthorizedResponse(@body body: UnauthorizedErrorResponse) {}
}

interface UpdateSurveyNotFoundErrorResponse extends NotFoundErrorResponseTemplate {
    message: "responseNotFound" | "timeslotNotFound"
}

interface UpdateSurveyUnprocessableEntityErrorResponse extends UnprocessableEntityErrorResponseTemplate {
    message: "noTimeslots" | "emptyTitle" | "titleTooLong" | "descriptionTooLong" | "invalidTimeslot"
}

interface UpdateSurveyForbiddenErrorResponse extends ForbiddenErrorResponseTemplate {
    message: "forbidden"
}
