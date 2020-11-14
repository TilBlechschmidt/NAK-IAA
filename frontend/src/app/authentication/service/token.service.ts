import {Injectable} from '@angular/core';
import {Jwt} from '../../api/models/jwt';

@Injectable({
    providedIn: 'root'
})
export class TokenService {

    private static storage = sessionStorage;

    constructor() {
    }

    getToken(): Jwt | null {
        return TokenService.storage.getItem('jwt');
    }

    isAuthenticated(): boolean {
        return TokenService.storage.getItem('jwt') != null;
    }

    deleteToken(): void {
        TokenService.storage.removeItem('jwt');
    }

    setToken(token: Jwt): void {
        TokenService.storage.setItem('jwt', token);
    }
}
