import {body, endpoint, headers, pathParams, request, response} from "@airtasker/spot";
import {
    AuthenticationHeaders,
    Identifier,
    MalformedRequestErrorResponse,
    NotFoundErrorResponse,
    UnauthorizedErrorResponse
} from "../types";
import {Survey} from "./types";

/** Fetches a survey by its unique identifier */
@endpoint({
    method: "GET",
    path: "/surveys/:id",
    tags: ["Surveys"]
})
class QuerySurvey {
    @request
    request(@headers headers: AuthenticationHeaders, @pathParams pathParams: QuerySurveyRequest) {}

    /** OK */
    @response({ status: 200 })
    successfulResponse(@body body: Survey) {}

    // MARK: - Generic response

    /** Resource not found */
    @response({ status: 404 })
    notFoundResponse(@body body: NotFoundErrorResponse) {}

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
