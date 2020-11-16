import {body, endpoint, pathParams, request, response} from "@airtasker/spot";
import {
    Identifier,
    MalformedRequestErrorResponse,
    NotFoundErrorResponseTemplate,
    UnauthorizedErrorResponse
} from "../../types";
import {ResponseDTO} from "./types";

/*
 * @author Til Blechschmidt
 */

/** Fetches a survey response by its unique identifier */
@endpoint({
    method: "GET",
    path: "/surveys/:surveyID/responses/:responseID",
    tags: ["Responses"]
})
class QueryResponse {
    @request
    request(@pathParams pathParams: QueryResponseRequest) {}

    /** OK */
    @response({ status: 200 })
    successfulResponse(@body body: ResponseDTO) {}

    // MARK: - Generic response

    /** Resource not found */
    @response({ status: 404 })
    notFoundResponse(@body body: QueryResponseNotFoundErrorResponse) {}

    /** Malformed request */
    @response({ status: 400 })
    malformedRequestResponse(@body body: MalformedRequestErrorResponse) {}

    /** Missing or invalid authentication */
    @response({ status: 401 })
    unauthorizedResponse(@body body: UnauthorizedErrorResponse) {}
}

interface QueryResponseRequest {
    /** Unique resource identifier for the survey */
    surveyID: Identifier;
    /** Unique resource identifier for the response */
    responseID: Identifier;
}

interface QueryResponseNotFoundErrorResponse extends NotFoundErrorResponseTemplate {
    message: "responseNotFound"
}
