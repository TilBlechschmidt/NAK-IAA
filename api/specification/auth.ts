import {endpoint, request, body, response} from "@airtasker/spot";
import {JWT, MalformedRequestErrorResponse, UnauthorizedErrorResponse} from "./types";
import {EMail, Password, User} from "./users/types";

/** Generates credentials to make authenticated requests with a limited lifetime */
@endpoint({
    method: "POST",
    path: "/authenticate",
    tags: ["Authentication"]
})
class Authenticate {
    @request
    request(@body body: AuthenticationRequest) {}

    /** Successfully authenticated */
    @response({ status: 200 })
    successfulResponse(@body body: AuthenticatedResponse) {}

    /** Invalid credentials */
    @response({ status: 403 })
    unauthorizedResponse(@body body: UnauthorizedErrorResponse) {}

    // MARK: - Generic response

    /** Malformed request */
    @response({ status: 400 })
    malformedRequestResponse(@body body: MalformedRequestErrorResponse) {}
}

interface AuthenticationRequest {
    email: EMail;
    password: Password;
}

interface AuthenticatedResponse extends User {
    token: JWT;
}
