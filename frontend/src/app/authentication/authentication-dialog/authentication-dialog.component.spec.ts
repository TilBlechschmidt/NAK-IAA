import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AuthenticationDialogComponent } from './authentication-dialog.component';
import {HttpClientTestingModule} from '@angular/common/http/testing';
import {TranslateModule} from '@ngx-translate/core';
import {RouterTestingModule} from '@angular/router/testing';
import {AccountService} from '../../api/services/account.service';
import {FormBuilder} from '@angular/forms';

describe('AuthenticationDialogComponent', () => {
  let component: AuthenticationDialogComponent;
  let fixture: ComponentFixture<AuthenticationDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AuthenticationDialogComponent ],
        imports: [RouterTestingModule, HttpClientTestingModule, TranslateModule.forRoot()],
        providers: [ AccountService, FormBuilder ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AuthenticationDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
