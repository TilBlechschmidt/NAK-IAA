import {Injectable} from '@angular/core';
import {JWT} from '../../../../../api/specification/types';

@Injectable({
    providedIn: 'root'
})
export class TokenService {

    constructor() {
    }

    getToken(): JWT {
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

    setToken(token: JWT): void {
        localStorage.setItem('jwt', token);
    }
}
