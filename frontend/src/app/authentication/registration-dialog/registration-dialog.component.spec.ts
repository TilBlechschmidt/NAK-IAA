import {ComponentFixture, TestBed} from '@angular/core/testing';

import {RegistrationDialogComponent} from './registration-dialog.component';
import {HttpClientTestingModule} from '@angular/common/http/testing';
import {FormBuilder} from '@angular/forms';
import {APP_INITIALIZER} from '@angular/core';
import {initApp} from '../../app.module';
import {HttpClient} from '@angular/common/http';
import {TranslateModule, TranslateService} from '@ngx-translate/core';
import {RouterTestingModule} from '@angular/router/testing';
import {AccountService} from '../../api/services/account.service';
import {MatSnackBarModule} from '@angular/material/snack-bar';

describe('RegistrationDialogComponent', () => {
    let component: RegistrationDialogComponent;
    let fixture: ComponentFixture<RegistrationDialogComponent>;

    beforeEach(async () => {
        await TestBed.configureTestingModule({
            declarations: [RegistrationDialogComponent],
            imports: [HttpClientTestingModule, RouterTestingModule, MatSnackBarModule, TranslateModule.forRoot()],
            providers: [AccountService, FormBuilder, {
                provide: APP_INITIALIZER,
                useFactory: initApp,
                deps: [HttpClient, TranslateService],
                multi: true
            }]
        }).compileComponents();
    });

    beforeEach(() => {
        fixture = TestBed.createComponent(RegistrationDialogComponent);
        component = fixture.componentInstance;
        fixture.detectChanges();
    });

    it('should create', () => {
        expect(component).toBeTruthy();
    });
});
