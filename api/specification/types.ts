import {Int64, String} from "@airtasker/spot";

/** Unique resource identifier */
export type Identifier = Int64;

/** [RFC 7519](https://tools.ietf.org/html/rfc7519) compliant authentication token */
export type JWT = String;

export interface AuthenticationHeaders {
    Authorization: JWT
}

export interface UnauthorizedErrorResponse {
    code: "unauthorized"
}

export interface MalformedRequestErrorResponse {
    code: "malformedRequest"
}

export interface NotFoundErrorResponse {
    code: "notFound"
}
