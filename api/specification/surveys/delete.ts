import {body, endpoint, pathParams, request, response} from "@airtasker/spot";
import {
    Identifier,
    MalformedRequestErrorResponse,
    GenericNotFoundErrorResponse,
    UnauthorizedErrorResponse, NotFoundErrorResponseTemplate, ForbiddenErrorResponseTemplate
} from "../types";
import {SurveyMetadataDTO} from "./types";

/*
 * @author Til Blechschmidt
 */

/** Irreversibly deletes a survey and all associated responses */
@endpoint({
    method: "DELETE",
    path: "/surveys/:id",
    tags: ["Surveys"]
})
class DeleteSurvey {
    @request
    request(@pathParams pathParams: DeleteSurveyRequest) {}

    /** Survey deleted */
    @response({ status: 200 })
    successfulResponse(@body body: SurveyMetadataDTO) {}

    /** Not Editable */
    @response({ status: 403 })
    forbiddenResponse(@body body: DeleteSurveyNotFoundErrorResponse) {}

    // MARK: - Generic response

    /** Resource not found */
    @response({ status: 404 })
    notFoundResponse(@body body: DeleteSurveyForbiddenErrorResponse) {}

    /** Malformed request */
    @response({ status: 400 })
    malformedRequestResponse(@body body: MalformedRequestErrorResponse) {}

    /** Missing or invalid authentication */
    @response({ status: 401 })
    unauthorizedResponse(@body body: UnauthorizedErrorResponse) {}
}

interface DeleteSurveyRequest {
    id: Identifier;
}

interface DeleteSurveyNotFoundErrorResponse extends NotFoundErrorResponseTemplate {
    message: "surveyNotFound"
}

interface DeleteSurveyForbiddenErrorResponse extends ForbiddenErrorResponseTemplate {
    message: "forbidden"
}

