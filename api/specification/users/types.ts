import {String} from '@airtasker/spot';
import {Identifier} from "../types";

/** Electronic mail address as defined in [RFC 5322 (Section 3.4.1)](https://tools.ietf.org/html/rfc5322#section-3.4.1) */
export type EMail = String;

/** 32 character ASCII string representing the SHA-512 hash of a users password */
export type Password = String;

export interface User {
    email: EMail;
    /** Full name of the associated person */
    name: String;
}

export interface IdentifiableUser extends User {
    id: Identifier;
}
