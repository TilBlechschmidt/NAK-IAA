import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
import {ActivatedRoute, Params, Router} from '@angular/router';
import {Jwt} from '../../api/models/jwt';
import {AccountService} from '../../api/services/account.service';
import {ActivateUserResponse} from '../../api/models/activate-user-response';

@Component({
    selector: 'app-password-confirmation-dialog',
    templateUrl: './password-confirmation-dialog.component.html',
    styleUrls: ['./password-confirmation-dialog.component.sass']
})
export class PasswordConfirmationDialogComponent implements OnInit {

    public form: FormGroup;
    private token: Jwt = '';
    public error = false;

    constructor(private formBuilder: FormBuilder, private route: ActivatedRoute, private accountService: AccountService,
                private router: Router) {
        this.form = this.formBuilder.group({
            password: new FormControl('', [Validators.required, Validators.minLength(16), Validators.maxLength(64)]),
            passwordConfirmation: new FormControl('', [Validators.required])
        });
        this.route.queryParams.subscribe((params: Params) => this.token = params.token);
    }

    ngOnInit(): void {
    }

    submit(): void {
        this.accountService.activateUser({
            body: {
                password: this.form.get('password')?.value,
                token: this.token
            }
        }).subscribe((next: ActivateUserResponse) =>
                this.router.navigateByUrl('/survey'), err => this.error = true
        );
    }

    isFormValid(): boolean {
        return !this.form.get('password')?.invalid
            && !this.form.get('passwordConfirmation')?.invalid
            && this.isConfirmationPasswordMatching();
    }

    isConfirmationPasswordMatching(): boolean {
        return this.form.get('password')?.value === this.form.get('passwordConfirmation')?.value;
    }

    isError(form: FormGroup, name: string): boolean | undefined {
        return form.get(name)?.touched && form.get(name)?.invalid;
    }
}
