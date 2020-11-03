import {body, endpoint, pathParams, request, response} from "@airtasker/spot";
import {
    Identifier,
    MalformedRequestErrorResponse,
    GenericNotFoundErrorResponse,
    UnauthorizedErrorResponse
} from "../types";
import {SurveyCreationMetadataDTO, SurveyMetadataDTO} from "./types";
import {CreateSurveyErrorResponse} from "./create";

/** Updates metadata of survey <strong>and deletes all responses</strong> */
@endpoint({
    method: "PUT",
    path: "/surveys/:id",
    tags: ["Surveys"]
})
class UpdateSurvey {
    @request
    request(@pathParams pathParams: { id: Identifier }, @body body: SurveyCreationMetadataDTO) {}

    /** Survey updated */
    @response({ status: 200 })
    successfulResponse(@body body: SurveyMetadataDTO) {}

    /** Invalid semantics */
    @response({ status: 422 })
    semanticErrorResponse(@body body: CreateSurveyErrorResponse) {}

    // MARK: - Generic response

    /** Resource not found */
    @response({ status: 404 })
    notFoundResponse(@body body: GenericNotFoundErrorResponse) {}

    /** Malformed request */
    @response({ status: 400 })
    malformedRequestResponse(@body body: MalformedRequestErrorResponse) {}

    /** Missing or invalid authentication */
    @response({ status: 401 })
    unauthorizedResponse(@body body: UnauthorizedErrorResponse) {}
}

