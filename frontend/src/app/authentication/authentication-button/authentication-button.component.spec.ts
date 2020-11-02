import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AuthenticationButtonComponent } from './authentication-button.component';
import {APP_INITIALIZER} from "@angular/core";
import {initApp} from "../../app.module";
import {HttpClient} from "@angular/common/http";
import {TranslateModule, TranslateService} from "@ngx-translate/core";
import {HttpClientTestingModule} from "@angular/common/http/testing";

describe('AuthenticationButtonComponent', () => {
  let component: AuthenticationButtonComponent;
  let fixture: ComponentFixture<AuthenticationButtonComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AuthenticationButtonComponent ],
        imports: [HttpClientTestingModule, TranslateModule.forRoot()],
        providers: [{
            provide: APP_INITIALIZER,
            useFactory: initApp,
            deps: [HttpClient, TranslateService],
            multi: true
        }]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AuthenticationButtonComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
