import {body, endpoint, request, response, String} from "@airtasker/spot";
import {EMail} from "./types";

/** Requests a token to create an account */
@endpoint({
    method: "POST",
    path: "/registrationEmail",
    tags: ["Account"]
})
class RequestRegistrationEmail {
    @request
    request(@body body: RequestRegistrationEmailRequest) {}

    /** Email will be send */
    @response({ status: 202 })
    successfulResponse(@body body: RequestRegistrationEmailResponse) {}
}

export interface RegistrationUserDTO {
    email: EMail;
    /** Full name of the associated person */
    name: String;
}

interface RequestRegistrationEmailRequest extends RegistrationUserDTO {}

interface RequestRegistrationEmailResponse extends RegistrationUserDTO {}
