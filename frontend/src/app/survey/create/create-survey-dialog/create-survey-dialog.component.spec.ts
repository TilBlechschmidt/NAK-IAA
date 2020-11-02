import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CreateSurveyDialogComponent } from './create-survey-dialog.component';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {TranslateModule} from "@ngx-translate/core";

describe('CreateSurveyDialogComponent', () => {
  let component: CreateSurveyDialogComponent;
  let fixture: ComponentFixture<CreateSurveyDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CreateSurveyDialogComponent ],
        imports: [TranslateModule.forRoot()],
        providers: [ { provide: MatDialogRef, useValue: {} }, { provide: MAT_DIALOG_DATA, useValue: {}} ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(CreateSurveyDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
