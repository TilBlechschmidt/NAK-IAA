import {body, endpoint, headers, pathParams, request, response} from "@airtasker/spot";
import {AuthenticationHeaders, Identifier, MalformedRequestErrorResponse, UnauthorizedErrorResponse} from "../types";
import {SurveyMetadata} from "./types";

/** Updates metadata of survey <strong>and deletes all responses</strong> */
@endpoint({
    method: "PUT",
    path: "/surveys/:id",
    tags: ["Surveys"]
})
class UpdateSurvey {
    @request
    request(@headers headers: AuthenticationHeaders, @pathParams pathParams: { id: Identifier }, @body body: SurveyMetadata) {}

    /** Survey updated */
    @response({ status: 200 })
    successfulResponse(@body body: SurveyMetadata) {}

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
