import {body, endpoint, headers, Int32, queryParams, request, response} from "@airtasker/spot";
import {AuthenticationHeaders, Identifier, MalformedRequestErrorResponse, UnauthorizedErrorResponse} from "../types";
import {SurveyMetadata, SurveyTitle} from "./types";

/** Retrieves a list of survey metadata with optional filters */
@endpoint({
    method: "GET",
    path: "/surveys",
    tags: ["Surveys"]
})
class QuerySurveys {
    @request
    request(@headers headers: AuthenticationHeaders, @queryParams queryParams: QuerySurveysRequest) {}

    /** OK */
    @response({ status: 200 })
    successfulResponse(@body body: QuerySurveysResponse) {}

    // MARK: - Generic response

    /** Malformed request */
    @response({ status: 400 })
    malformedRequestResponse(@body body: MalformedRequestErrorResponse) {}

    /** Missing or invalid authentication */
    @response({ status: 401 })
    unauthorizedResponse(@body body: UnauthorizedErrorResponse) {}
}

interface QuerySurveysRequest {
    /** Return only upcoming surveys */
    isUpcoming?: boolean;
    /** Return only completed surveys */
    isCompleted?: boolean;
    /** Return only surveys which have been modified by the owner after a response has been given */
    requiresAttention?: boolean;
    /** Return only surveys created by the authenticated user */
    isOwnSurvey?: boolean;
    /** Return only surveys the authenticated user has participated in */
    didParticipateIn?: boolean;
}

interface QuerySurveysResult {
    id: Identifier;
    title: SurveyTitle;
    /** Number of participants of a survey */
    participantCount: Int32;
}

interface QuerySurveysResponse {
    /** List of surveys matching the search criteria */
    surveys: QuerySurveysResult[];
}
