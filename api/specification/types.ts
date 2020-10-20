import {Int32, String} from "@airtasker/spot";

/** Unique resource identifier */
export type Identifier = Int32;

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
