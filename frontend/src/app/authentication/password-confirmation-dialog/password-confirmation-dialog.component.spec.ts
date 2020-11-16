import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PasswordConfirmationDialogComponent } from './password-confirmation-dialog.component';
import {HttpClientModule} from '@angular/common/http';
import {RouterTestingModule} from '@angular/router/testing';
import {TranslateModule} from '@ngx-translate/core';
import {FormBuilder} from '@angular/forms';
import {MatSnackBarModule} from '@angular/material/snack-bar';

describe('PasswordConfirmationDialogComponent', () => {
  let component: PasswordConfirmationDialogComponent;
  let fixture: ComponentFixture<PasswordConfirmationDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
        imports: [ HttpClientModule, RouterTestingModule, MatSnackBarModule, TranslateModule.forRoot() ],
        providers: [ FormBuilder ],
        declarations: [ PasswordConfirmationDialogComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(PasswordConfirmationDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
