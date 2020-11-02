import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RegistrationButtonComponent } from './registration-button.component';
import {APP_INITIALIZER} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {TranslateModule, TranslateService} from "@ngx-translate/core";
import {initApp} from "../../app.module";
import {HttpClientTestingModule} from "@angular/common/http/testing";

describe('RegistrationButtonComponent', () => {
  let component: RegistrationButtonComponent;
  let fixture: ComponentFixture<RegistrationButtonComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ RegistrationButtonComponent ],
        imports: [HttpClientTestingModule, TranslateModule.forRoot()],
        providers: [
            HttpClient,
            {
            provide: APP_INITIALIZER,
            useFactory: initApp,
            deps: [HttpClient, TranslateService],
            multi: true
        }]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(RegistrationButtonComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
