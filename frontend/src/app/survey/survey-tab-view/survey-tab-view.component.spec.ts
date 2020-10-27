import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SurveyTabViewComponent } from './survey-tab-view.component';

describe('SurveyTabViewComponent', () => {
  let component: SurveyTabViewComponent;
  let fixture: ComponentFixture<SurveyTabViewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ SurveyTabViewComponent ]
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
