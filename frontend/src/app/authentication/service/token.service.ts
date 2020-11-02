import {Injectable} from '@angular/core';
import {JWT} from "../../../../../api/specification/types";

@Injectable({
    providedIn: 'root'
})
export class TokenService {

    constructor() {
    }

    isAuthenticated() {
        console.log(localStorage.getItem("jwt"));
        return localStorage.getItem("jwt") != null
    }

    deleteToken() {
        localStorage.removeItem("jwt");
    }

    setToken(token: JWT) {
        localStorage.setItem("jwt", token);
    }

    getUsername() {

    }

    getUserId() {

    }

}
