import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AuthenticationDialogComponent } from './authentication-dialog.component';
import {AuthenticationService} from "../../api/services";
import {HttpClient} from "@angular/common/http";
import {HttpClientTestingModule} from "@angular/common/http/testing";
import {TranslateModule} from "@ngx-translate/core";
import {RouterTestingModule} from "@angular/router/testing";

describe('AuthenticationDialogComponent', () => {
  let component: AuthenticationDialogComponent;
  let fixture: ComponentFixture<AuthenticationDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AuthenticationDialogComponent ],
        imports: [RouterTestingModule, HttpClientTestingModule, TranslateModule.forRoot()],
        providers: [ AuthenticationService]
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
