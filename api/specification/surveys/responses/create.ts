import {body, headers, endpoint, request, response, pathParams} from "@airtasker/spot";
import {AuthenticationHeaders, Identifier, MalformedRequestErrorResponse, UnauthorizedErrorResponse} from "../../types";
import {Response, ResponseValue} from "./types";

/** Responds to an existing survey as the user associated with the provided credentials */
@endpoint({
    method: "POST",
    path: "/surveys/:surveyID/responses",
    tags: ["Responses"]
})
class CreateResponse {
    @request
    request(@headers headers: AuthenticationHeaders, @pathParams pathParams: CreateResponsePathParams, @body body: CreateResponseRequest) {}

    /** Response submitted */
    @response({ status: 201 })
    successfulResponse(@body body: Response) {}

    /** Invalid semantics */
    @response({ status: 422 })
    semanticErrorResponse(@body body: CreateResponseErrorResponse) {}

    /** Duplicate response */
    @response({ status: 409 })
    duplicateResponse(@body body: CreateResponseDuplicateErrorResponse) {}

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
    values: ResponseValue[];
}

export interface CreateResponseErrorResponse {
    code: "invalidEnumValuesProvided" | "valueCountMismatch";
}

interface CreateResponseDuplicateErrorResponse {
    /** Indicates that the response has already been submitted and a `PUT` request should be used on the existing resource indicated by the `id` field */
    code: "responseAlreadySubmitted";
    id: Identifier;
}
