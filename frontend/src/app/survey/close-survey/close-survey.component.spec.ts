import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CloseSurveyComponent } from './close-survey.component';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material/dialog';
import {HttpClientTestingModule} from '@angular/common/http/testing';
import {TranslateModule} from '@ngx-translate/core';
import {RouterTestingModule} from '@angular/router/testing';

describe('CloseSurveyComponent', () => {
  let component: CloseSurveyComponent;
  let fixture: ComponentFixture<CloseSurveyComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CloseSurveyComponent ],
        imports: [HttpClientTestingModule, TranslateModule.forRoot(), RouterTestingModule],
        providers: [{provide: MatDialogRef, useValue: {}},
            {provide: MAT_DIALOG_DATA, useValue: {timeslot: {start: '2020-11-14T12:30:00.000Z', end: '2020-11-14T13:00:00.000Z'}}}]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(CloseSurveyComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
