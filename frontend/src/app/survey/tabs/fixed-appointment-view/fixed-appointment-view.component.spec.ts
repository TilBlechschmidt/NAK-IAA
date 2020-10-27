import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FixedAppointmentViewComponent } from './fixed-appointment-view.component';

describe('FixedAppointmentViewComponent', () => {
  let component: FixedAppointmentViewComponent;
  let fixture: ComponentFixture<FixedAppointmentViewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ FixedAppointmentViewComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(FixedAppointmentViewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
