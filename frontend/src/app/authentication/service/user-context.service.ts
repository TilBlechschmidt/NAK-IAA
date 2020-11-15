import {Injectable} from '@angular/core';

/**
 * @author Hendrik Reiter
 */

@Injectable({
    providedIn: 'root'
})
export class UserContextService {

    constructor() {
    }

    setUserName(username: string): void {
        localStorage.setItem('username', username);
    }

    getUserName(): string {
        const username = localStorage.getItem('username');
        if (username === null) {
            return '';
        } else {
            return username;
        }
    }

    setLanguage(langCode: string): void {
        localStorage.setItem('locale', langCode);
    }
}
