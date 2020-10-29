import {body, endpoint, request, response} from "@airtasker/spot";
import {IdentifiableUser, Password, User} from "./types";
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

interface CreateUserRequest extends User {
    password: Password;
}

interface CreateUserResponse extends IdentifiableUser {}

interface CreateUserErrorResponse {
    code: "emailDuplicated";
}
