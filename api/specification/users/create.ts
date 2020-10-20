import {body, endpoint, request, response, String} from "@airtasker/spot";
import {EMail, Password} from "./types";
import {Identifier} from "../types";

/** Registers a new user account */
@endpoint({
    method: "POST",
    path: "/users",
    tags: ["Authentication"]
})
class CreateUser {
    @request
    request(@body body: CreateUserRequest) {}

    /** User created */
    @response({ status: 201 })
    successfulResponse(@body body: CreateUserResponse) {}

    /** Already registered */
    @response({ status: 409 })
    conflictResponse(@body body: CreateUserErrorResponse) {}
}

export interface User {
    email: EMail;
    /** Full name of the associated person */
    name: String;
}

interface CreateUserRequest extends User {
    password: Password;
}

interface CreateUserResponse extends User {
    id: Identifier;
}

interface CreateUserErrorResponse {
    code: "emailDuplicated";
}
