import {String} from '@airtasker/spot';
import {Identifier} from "../types";

/*
 * @author Til Blechschmidt
 */

/** Electronic mail address as defined in [RFC 5322 (Section 3.4.1)](https://tools.ietf.org/html/rfc5322#section-3.4.1) */
export type EMail = String;

/** 32 character ASCII string representing the SHA-512 hash of a users password */
export type Password = String;

interface NamedUserDTO {
    /** Full name of the associated person */
    name: String;
}

export interface UserDTO extends NamedUserDTO {
    email: EMail;
}

export interface IdentifiableUserDTO extends NamedUserDTO {
    id: Identifier;
}
