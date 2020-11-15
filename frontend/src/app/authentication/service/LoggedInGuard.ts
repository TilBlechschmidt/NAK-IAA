import {ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot, UrlTree} from '@angular/router';
import {Observable} from 'rxjs';
import {TokenService} from './token.service';
import {Injectable} from '@angular/core';

/**
 * @author Hendrik Reiter
 */

@Injectable()
export class LoggedInGuard implements CanActivate {

    constructor(private tokenService: TokenService, private router: Router) {}

    canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot)
        : Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
        if (this.tokenService.isAuthenticated()){
            return true;
        } else {
            this.router.navigateByUrl('sign_in');
            return false;
        }
    }

}
