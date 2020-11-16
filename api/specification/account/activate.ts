import {body, endpoint, request, response, String} from "@airtasker/spot";
import {Password} from "./types";
import {
    ConflictErrorResponseTemplate,
    Identifier,
    UnauthorizedErrorResponseTemplate,
    UnprocessableEntityErrorResponseTemplate
} from '../types';
import {RegistrationUserDTO} from "./request";

/*
 * @author Noah Peeters
 */

/** Registers a new user account */
@endpoint({
    method: "POST",
    path: "/users",
    tags: ["Account"]
})
class ActivateUser {
    @request
    request(@body body: ActivateUserRequest) {}

    /** User created */
    @response({ status: 201 })
    successfulResponse(@body body: ActivateUserResponse) {}

    /** Password Invalid */
    @response({ status: 422 })
    passwordInvalidResponse(@body body: ActivateUserUnprocessableEntityError) {}

    /** Duplicate response */
    @response({ status: 409 })
    duplicateResponse(@body body: ActivateUserConflictErrorResponse) {}

    /** Missing or invalid authentication */
    @response({ status: 401 })
    unauthorizedResponse(@body body: ActivateUserUnauthorizedErrorResponse) {}
}

interface ActivateUserRequest {
    token: String;
    password: Password;
}

interface ActivateUserResponse extends RegistrationUserDTO {
    id: Identifier;
}

interface ActivateUserUnauthorizedErrorResponse extends UnauthorizedErrorResponseTemplate {
    message: "invalidToken" | "missingClaims"
}

interface ActivateUserConflictErrorResponse extends ConflictErrorResponseTemplate {
    message: "emailDuplicate"
}

interface ActivateUserUnprocessableEntityError extends UnprocessableEntityErrorResponseTemplate {
    message: "passwordDoesNotMatchRules"
}
