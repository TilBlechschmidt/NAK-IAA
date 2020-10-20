import {body, endpoint, headers, pathParams, request, response} from "@airtasker/spot";
import {AuthenticationHeaders, Identifier, MalformedRequestErrorResponse, UnauthorizedErrorResponse} from "../../types";
import {Response} from "./types";

/** Fetches a survey response by its unique identifier */
@endpoint({
    method: "GET",
    path: "/surveys/:surveyID/responses/:responseID",
    tags: ["Responses"]
})
class QueryResponse {
    @request
    request(@headers headers: AuthenticationHeaders, @pathParams pathParams: QueryResponseRequest) {}

    /** OK */
    @response({ status: 200 })
    successfulResponse(@body body: Response) {}

    // MARK: - Generic response

    /** Resource not found */
    @response({ status: 404 })
    notFoundResponse() {}

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
