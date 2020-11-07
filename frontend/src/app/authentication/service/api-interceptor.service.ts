import {Injectable} from '@angular/core';
import {HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from '@angular/common/http';
import {Observable} from 'rxjs';
import {TokenService} from './token.service';
import {tap} from 'rxjs/operators';

@Injectable({
    providedIn: 'root'
})
export class ApiInterceptorService implements HttpInterceptor {

    constructor(private tokenService: TokenService) {
    }

    // tslint:disable-next-line:no-any
    intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
        req = req.clone({
            setHeaders: {
                Authorization: this.tokenService.getToken()
            }
        });

        return next.handle(req).pipe(
            tap(x => x, err => {
                console.error(`Error performing request, status code = ${err.status}`);
            })
        );
    }
}

