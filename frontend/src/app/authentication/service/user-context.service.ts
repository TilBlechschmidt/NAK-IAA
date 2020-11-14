import {Injectable} from '@angular/core';

@Injectable({
    providedIn: 'root'
})
export class UserContextService {

    username?: string;

    constructor() {
    }

    setUserName(username: string) {
        this.username = username
    }

    getUserName(): string {
        return this.username ? this.username : "";
    }

    setLanguage(langCode: string) {
        localStorage.setItem('locale', langCode);
    }
}
