import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CloseSurveyButtonComponent } from './close-survey-button.component';
import {MatDialog} from '@angular/material/dialog';
import {TranslateModule} from '@ngx-translate/core';

describe('CloseSurveyButtonComponent', () => {
  let component: CloseSurveyButtonComponent;
  let fixture: ComponentFixture<CloseSurveyButtonComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CloseSurveyButtonComponent ],
        imports: [TranslateModule.forRoot()],
        providers: [{provide: MatDialog, useValue: {}}]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(CloseSurveyButtonComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
