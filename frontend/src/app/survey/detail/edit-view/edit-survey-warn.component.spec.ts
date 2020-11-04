import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EditSurveyWarnComponent } from './edit-survey-warn.component';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material/dialog';
import {HttpClientTestingModule} from '@angular/common/http/testing';
import {RouterTestingModule} from '@angular/router/testing';
import {TranslateModule, TranslateService} from '@ngx-translate/core';

describe('EditViewComponent', () => {
  let component: EditSurveyWarnComponent;
  let fixture: ComponentFixture<EditSurveyWarnComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ EditSurveyWarnComponent ],
      imports: [ HttpClientTestingModule, RouterTestingModule, TranslateModule.forRoot() ],
      providers: [{provide: MatDialogRef, useValue: {}},
            {provide: MAT_DIALOG_DATA, useValue: {}}]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(EditSurveyWarnComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
