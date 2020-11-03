import {body, endpoint, request, response, String} from "@airtasker/spot";
import {EMail} from "./types";
import {ServiceUnavailableErrorResponseTemplate} from "../types";

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

    /** Email cannot be send */
    @response({ status: 503 })
    serviceUnavailableResponse(@body body: RequestRegistrationEmailServiceUnavailableErrorResponse) {}
}

export interface RegistrationUserDTO {
    email: EMail;
    /** Full name of the associated person */
    name: String;
}

interface RequestRegistrationEmailRequest extends RegistrationUserDTO {}

interface RequestRegistrationEmailResponse extends RegistrationUserDTO {}

interface RequestRegistrationEmailServiceUnavailableErrorResponse extends  ServiceUnavailableErrorResponseTemplate {
    message: "mailError"
}
