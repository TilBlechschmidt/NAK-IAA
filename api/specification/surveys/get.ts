import {body, endpoint, pathParams, request, response} from "@airtasker/spot";
import {
    Identifier,
    MalformedRequestErrorResponse, NotFoundErrorResponseTemplate,
    UnauthorizedErrorResponse
} from "../types";
import {SurveyDTO} from "./types";

/** Fetches a survey by its unique identifier */
@endpoint({
    method: "GET",
    path: "/surveys/:id",
    tags: ["Surveys"]
})
class QuerySurvey {
    @request
    request(@pathParams pathParams: QuerySurveyRequest) {}

    /** OK */
    @response({ status: 200 })
    successfulResponse(@body body: SurveyDTO) {}

    // MARK: - Generic response

    /** Resource not found */
    @response({ status: 404 })
    notFoundResponse(@body body: QuerySurveyNotFoundErrorResponse) {}

    /** Malformed request */
    @response({ status: 400 })
    malformedRequestResponse(@body body: MalformedRequestErrorResponse) {}

    /** Missing or invalid authentication */
    @response({ status: 401 })
    unauthorizedResponse(@body body: UnauthorizedErrorResponse) {}
}

interface QuerySurveyRequest {
    id: Identifier;
}

interface QuerySurveyNotFoundErrorResponse extends NotFoundErrorResponseTemplate {
    message: "surveyNotFound"
}
