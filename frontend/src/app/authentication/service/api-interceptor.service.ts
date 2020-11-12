import {Injectable} from '@angular/core';
import {HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from '@angular/common/http';
import {Observable} from 'rxjs';
import {TokenService} from './token.service';
import {tap} from 'rxjs/operators';
import {Router} from '@angular/router';

@Injectable({
    providedIn: 'root'
})
export class ApiInterceptorService implements HttpInterceptor {

    constructor(private tokenService: TokenService, private router: Router) {
    }

    // tslint:disable-next-line:no-any
    intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {

        const token = this.tokenService.getToken();

        if (token) {
            req = req.clone({
                setHeaders: {
                    Authorization: token
                }
            });
        }

        return next.handle(req).pipe(
            tap(x => x, err => {
                if (err.status === 401) {
                    this.tokenService.deleteToken();
                    this.router.navigateByUrl('sign_in');
                } else {
                    console.error(`Error performing request, status code = ${err.status}`);
                }
            })
        );
    }
}

