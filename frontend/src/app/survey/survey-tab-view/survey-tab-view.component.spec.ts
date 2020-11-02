import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SurveyTabViewComponent } from './survey-tab-view.component';
import {APP_INITIALIZER} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {TranslateModule, TranslateService} from "@ngx-translate/core";
import {initApp} from "../../app.module";
import {HttpClientTestingModule} from "@angular/common/http/testing";

describe('SurveyTabViewComponent', () => {
  let component: SurveyTabViewComponent;
  let fixture: ComponentFixture<SurveyTabViewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ SurveyTabViewComponent ],
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
    fixture = TestBed.createComponent(SurveyTabViewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
