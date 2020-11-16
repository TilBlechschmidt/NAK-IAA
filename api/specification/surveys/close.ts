import {body, endpoint, pathParams, request, response} from "@airtasker/spot";
import {
    Identifier,
    MalformedRequestErrorResponse,
    UnauthorizedErrorResponse,
    NotFoundErrorResponseTemplate,
    ForbiddenErrorResponseTemplate,
    BadRequestErrorResponseTemplate
} from "../types";
import {SurveyMetadataDTO} from "./types";

/*
 * @author Til Blechschmidt
 */

/** Ends the survey and notifies all participants */
@endpoint({
    method: "PATCH",
    path: "/surveys/:id",
    tags: ["Surveys"]
})
class CloseSurvey {
    @request
    request(@pathParams pathParams: { id: Identifier }, @body body: CloseSurveyRequest) {}

    /** Survey closed */
    @response({ status: 200 })
    successfulResponse(@body body: SurveyMetadataDTO) {}

    /** Not Closable */
    @response({ status: 403 })
    forbiddenResponse(@body body: CloseSurveyForbiddenErrorResponse) {}

    // MARK: - Generic response

    /** Resource not found */
    @response({ status: 404 })
    notFoundResponse(@body body: CloseSurveyNotFoundErrorResponse) {}

    /** Malformed request */
    @response({ status: 400 })
    malformedRequestResponse(@body body: CloseSurveyMalformedRequestErrorResponse) {}

    /** Missing or invalid authentication */
    @response({ status: 401 })
    unauthorizedResponse(@body body: UnauthorizedErrorResponse) {}
}

interface CloseSurveyRequest {
    operation: "close";
    /** Unique resource identifier for the timeslot that is going to take effect */
    selectedTimeslot: Identifier;
}

interface CloseSurveyNotFoundErrorResponse extends NotFoundErrorResponseTemplate {
    message: "timeslotNotFound" | "surveyNotFound"
}

interface CloseSurveyForbiddenErrorResponse extends ForbiddenErrorResponseTemplate {
    message: "forbidden"
}

interface CloseSurveyMalformedRequestErrorResponse extends BadRequestErrorResponseTemplate {
    message: "malformedRequest" | "unsupportedOperation"
}
