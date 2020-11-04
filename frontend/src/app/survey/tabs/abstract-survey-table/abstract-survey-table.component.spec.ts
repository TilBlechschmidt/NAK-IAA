import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AbstractSurveyTableComponent } from './abstract-survey-table.component';
import {HttpClientTestingModule} from "@angular/common/http/testing";

describe('AbstractSurveyTableComponent', () => {
  let component: AbstractSurveyTableComponent;
  let fixture: ComponentFixture<AbstractSurveyTableComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AbstractSurveyTableComponent ],
      imports: [ HttpClientTestingModule ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AbstractSurveyTableComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
