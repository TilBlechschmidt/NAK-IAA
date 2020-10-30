import {body, endpoint, pathParams, request, response} from "@airtasker/spot";
import {
    Identifier,
    MalformedRequestErrorResponse,
    NotFoundErrorResponse,
    UnauthorizedErrorResponse
} from "../types";
import {SurveyMetadata} from "./types";

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
    @response({ status: 204 })
    successfulResponse(@body body: SurveyMetadata) {}

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

interface DeleteSurveyRequest {
    id: Identifier;
}
