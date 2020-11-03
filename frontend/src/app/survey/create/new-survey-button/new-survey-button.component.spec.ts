import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NewSurveyButtonComponent } from './new-survey-button.component';
import {MatDialog, MatDialogModule} from '@angular/material/dialog';
import {APP_INITIALIZER} from '@angular/core';
import {initApp} from '../../../app.module';
import {HttpClient} from '@angular/common/http';
import {TranslateModule, TranslateService} from '@ngx-translate/core';
import {HttpClientTestingModule} from '@angular/common/http/testing';

describe('NewSurveyButtonComponent', () => {
  let component: NewSurveyButtonComponent;
  let fixture: ComponentFixture<NewSurveyButtonComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ NewSurveyButtonComponent ],
      imports: [ MatDialogModule, HttpClientTestingModule, TranslateModule.forRoot()],
        providers : [{
            provide: APP_INITIALIZER,
            useFactory: initApp,
            deps: [HttpClient, TranslateService],
            multi: true
        }]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(NewSurveyButtonComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
