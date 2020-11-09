import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CloseSurveyButtonComponent } from './close-survey-button.component';

describe('CloseSurveyButtonComponent', () => {
  let component: CloseSurveyButtonComponent;
  let fixture: ComponentFixture<CloseSurveyButtonComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CloseSurveyButtonComponent ]
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
