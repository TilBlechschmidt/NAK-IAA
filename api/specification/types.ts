import {DateTime, Int64, String} from "@airtasker/spot";

/*
 * @author Noah Peeters & Til Blechschmidt
 */

/** Unique resource identifier */
export type Identifier = Int64;

/** [RFC 7519](https://tools.ietf.org/html/rfc7519) compliant authentication token */
export type JWT = String;

export interface AuthenticationHeaders {
    Authorization: JWT
}

export interface ErrorResponse {
    timestamp: DateTime
    status: number
    error: String
    message: String
    path: String
}

export interface ForbiddenErrorResponseTemplate extends ErrorResponse {
    error: "Forbidden"
    status: 403
}

export interface BadRequestErrorResponseTemplate extends ErrorResponse {
    error: "Bad Request"
    status: 400
}

export interface NotFoundErrorResponseTemplate extends ErrorResponse {
    error: "Not Found"
    status: 404
}

export interface UnprocessableEntityErrorResponseTemplate extends ErrorResponse {
    error: "Unprocessable Entity"
    status: 422
}

export interface ConflictErrorResponseTemplate extends ErrorResponse {
    error: "Conflict"
    status: 409
}

export interface UnauthorizedErrorResponseTemplate extends ErrorResponse {
    error: "Unauthorized"
    status: 401
}

export interface ServiceUnavailableErrorResponseTemplate extends ErrorResponse {
    error: "Service Unavailable"
    status: 503
}

export interface UnauthorizedErrorResponse extends UnauthorizedErrorResponseTemplate {
    message: "accessDenied"
}

export interface MalformedRequestErrorResponse extends BadRequestErrorResponseTemplate {
    message: "malformedRequest"
}

export interface GenericNotFoundErrorResponse extends NotFoundErrorResponseTemplate {
    message: "TODO"
}
