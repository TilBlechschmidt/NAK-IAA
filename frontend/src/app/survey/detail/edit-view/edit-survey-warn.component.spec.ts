import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EditSurveyWarnComponent } from './edit-survey-warn.component';

describe('EditViewComponent', () => {
  let component: EditSurveyWarnComponent;
  let fixture: ComponentFixture<EditSurveyWarnComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ EditSurveyWarnComponent ]
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
