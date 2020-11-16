import {endpoint, request, body, response} from "@airtasker/spot";
import {JWT, MalformedRequestErrorResponse, UnauthorizedErrorResponseTemplate} from "../types";
import {EMail, Password, UserDTO} from "./types";

/*
 * @author Til Blechschmidt & Noah Peeters
 */

/** Generates credentials to make authenticated requests with a limited lifetime */
@endpoint({
    method: "POST",
    path: "/authenticate",
    tags: ["Account"]
})
class Authenticate {
    @request
    request(@body body: AuthenticationRequest) {}

    /** Successfully authenticated */
    @response({ status: 200 })
    successfulResponse(@body body: AuthenticatedResponse) {}

    /** Missing or invalid authentication */
    @response({ status: 401 })
    unauthorizedResponse(@body body: AuthenticateUnauthorizedErrorResponse) {}

    // MARK: - Generic response

    /** Malformed request */
    @response({ status: 400 })
    malformedRequestResponse(@body body: MalformedRequestErrorResponse) {}
}

interface AuthenticationRequest {
    email: EMail;
    password: Password;
}

interface AuthenticatedResponse extends UserDTO {
    token: JWT;
}

interface AuthenticateUnauthorizedErrorResponse extends UnauthorizedErrorResponseTemplate {
    message: "invalidAuthenticationData"
}
