import {Injectable} from '@angular/core';

@Injectable({
    providedIn: 'root'
})
export class UserContextService {

    username?: string;

    constructor() {
    }

    setUserName(username: string): void {
        this.username = username;
    }

    getUserName(): string {
        return this.username ? this.username : '';
    }

    setLanguage(langCode: string): void {
        localStorage.setItem('locale', langCode);
    }
}
