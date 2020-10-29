import {body, headers, endpoint, request, response, pathParams} from "@airtasker/spot";
import {
    AuthenticationHeaders,
    Identifier,
    MalformedRequestErrorResponse,
    NotFoundErrorResponse,
    UnauthorizedErrorResponse
} from "../../types";
import {Response, ResponseValue} from "./types";
import {CreateResponseErrorResponse, CreateResponseRequest} from "./create";

/** Updates an existing response */
@endpoint({
    method: "PATCH",
    path: "/surveys/:surveyID/responses/:responseID",
    tags: ["Responses"]
})
class UpdateResponse {
    @request
    request(@headers headers: AuthenticationHeaders, @pathParams pathParams: UpdateResponsePathParams, @body body: CreateResponseRequest) {}

    /** Response submitted */
    @response({ status: 201 })
    successfulResponse(@body body: Response) {}

    /** Invalid semantics */
    @response({ status: 422 })
    semanticErrorResponse(@body body: CreateResponseErrorResponse) {}

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

interface UpdateResponsePathParams {
    /** Unique resource identifier for the survey */
    surveyID: Identifier;
    /** Unique resource identifier for the response */
    responseID: Identifier;
}
