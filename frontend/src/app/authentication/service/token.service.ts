import {Injectable} from '@angular/core';
import {Jwt} from '../../api/models/jwt';

@Injectable({
    providedIn: 'root'
})
export class TokenService {

    constructor() {
    }

    getToken(): Jwt {
        const token = localStorage.getItem('jwt');
        if (token === null) {
            return '';
        } else {
            return token;
        }
    }

    isAuthenticated(): boolean {
        return localStorage.getItem('jwt') != null;
    }

    deleteToken(): void {
        localStorage.removeItem('jwt');
    }

    setToken(token: Jwt): void {
        localStorage.setItem('jwt', token);
    }
}
