import {body, endpoint, pathParams, request, response} from "@airtasker/spot";
import {
    Identifier,
    MalformedRequestErrorResponse,
    NotFoundErrorResponse,
    UnauthorizedErrorResponse
} from "../types";
import {SurveyMetadata} from "./types";

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

interface CloseSurveyRequest {
    operation: "close";
    /** Unique resource identifier for the timeslot that is going to take effect */
    selectedTimeslot: Identifier;
}
