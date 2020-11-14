import {Component, OnInit} from '@angular/core';
import {AuthenticatedResponse} from '../../api/models';
import {Router} from '@angular/router';
import {TokenService} from '../service/token.service';
import {AccountService} from '../../api/services/account.service';
import {FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
import {UserContextService} from '../service/user-context.service';


@Component({
    selector: 'app-authentication-dialog',
    templateUrl: './authentication-dialog.component.html',
    styleUrls: ['./authentication-dialog.component.sass']
})
export class AuthenticationDialogComponent implements OnInit {

    public form: FormGroup;

    authError = false;

    constructor(private accountService: AccountService, private authService: TokenService,
                private router: Router, private formBuilder: FormBuilder, private userContextService: UserContextService) {
        this.form = this.formBuilder.group({
            email: new FormControl('', [Validators.required]),
            password: new FormControl('', [Validators.required]),
        });
    }

    ngOnInit(): void {
    }

    signIn(): void {
        this.accountService.authenticate({
            body: {
                email: this.form.get('email')?.value,
                password: this.form.get('password')?.value
            }
        }).subscribe(
            (next: AuthenticatedResponse) => {
                this.authService.setToken(next.token);
                this.router.navigateByUrl('survey');
                this.userContextService.setUserName(next.name)
            }, (err: AuthenticatedResponse) => this.authError = true
        );
    }

}
