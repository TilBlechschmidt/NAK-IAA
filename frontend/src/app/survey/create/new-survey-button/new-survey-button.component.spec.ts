import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NewSurveyButtonComponent } from './new-survey-button.component';

describe('NewSurveyButtonComponent', () => {
  let component: NewSurveyButtonComponent;
  let fixture: ComponentFixture<NewSurveyButtonComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ NewSurveyButtonComponent ]
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
