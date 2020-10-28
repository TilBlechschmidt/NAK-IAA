import {Component, OnInit} from '@angular/core';
import {AuthenticationService} from "../../api/services";
import {FormBuilder, FormControl, FormGroup, Validators} from "@angular/forms";

@Component({
    selector: 'app-registration-dialog',
    templateUrl: './registration-dialog.component.html',
    styleUrls: ['./registration-dialog.component.sass']
})
export class RegistrationDialogComponent implements OnInit {

    public form: FormGroup;

    constructor(public service: AuthenticationService, protected readonly formBuilder: FormBuilder) {
        this.form = this.formBuilder.group({
            name: new FormControl('', Validators.required),
            email: new FormControl('', [Validators.required, Validators.email]),
            password: new FormControl('', [Validators.required, Validators.minLength(16), Validators.maxLength(64)]),
            passwordConfirmation: new FormControl('', [Validators.required])
        }, {validator: this.checkPasswords});
    }

    ngOnInit(): void {
    }

    submit() {
        if (this.isFormValid()) {
            this.service.createUser({
                body: {
                    name: this.form.get('name')?.value,
                    email: this.form.get('email')?.value,
                    password: this.form.get('password')?.value
                }
            }).subscribe(next => console.log(next), error => console.log(error))
        }
    }

    isFormValid(): boolean {
        return !this.form.get('name')?.invalid
            && !this.form.get('email')?.invalid
            && !this.form.get('password')?.invalid
            && this.isConfirmationPasswordMatching()
    }

    isError(form: FormGroup, name: string) {
        return form.get(name)?.touched && form.get(name)?.invalid
    }

    isConfirmationPasswordMatching() : boolean {
        return this.form.get('password')?.value === this.form.get('passwordConfirmation')?.value;
    }

    checkPasswords(group: FormGroup) {
        let pass = group.get('password')?.value;
        let confirmPass = group.get('passwordConfirmation')?.value;
        return pass === confirmPass ? null : {notSame: true}
    }
}

