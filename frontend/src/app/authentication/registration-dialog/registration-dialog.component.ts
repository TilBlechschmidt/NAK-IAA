import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
import {Router} from '@angular/router';
import {AccountService} from '../../api/services/account.service';
import {RequestRegistrationEmailResponse} from '../../api/models/request-registration-email-response';

@Component({
    selector: 'app-registration-dialog',
    templateUrl: './registration-dialog.component.html',
    styleUrls: ['./registration-dialog.component.sass']
})
export class RegistrationDialogComponent implements OnInit {

    public form: FormGroup;

    constructor(public service: AccountService, protected readonly formBuilder: FormBuilder, private router: Router) {
        this.form = this.formBuilder.group({
            name: new FormControl('', Validators.required),
            email: new FormControl('', [Validators.required, Validators.email])
        });
    }

    ngOnInit(): void {
    }

    submit(): void {
        if (this.isFormValid()) {
            this.service.requestRegistrationEmail({
                body: {
                    name: this.form.get('name')?.value,
                    email: this.form.get('email')?.value
                }
            }).subscribe((next: RequestRegistrationEmailResponse) => this.router.navigateByUrl('/survey'));
        }
    }

    isFormValid(): boolean {
        return !this.form.get('name')?.invalid
            && !this.form.get('email')?.invalid;
    }

    isError(form: FormGroup, name: string): boolean | undefined {
        return form.get(name)?.touched && form.get(name)?.invalid;
    }


}

