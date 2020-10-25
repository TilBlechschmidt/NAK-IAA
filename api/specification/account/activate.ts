import {body, endpoint, request, response, String} from "@airtasker/spot";
import {Password} from "./types";
import {Identifier} from "../types";
import {User} from "./request";

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
}

interface ActivateUserRequest {
    token: String;
    password: Password;
}

interface ActivateUserResponse extends User {
    id: Identifier;
}
