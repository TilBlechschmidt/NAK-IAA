import {Component, Input, OnInit} from '@angular/core';
import {MatDialog} from '@angular/material/dialog';
import {CloseSurveyComponent} from '../close-survey.component';
import {TimeslotDto} from '../../../api/models/timeslot-dto';

@Component({
    selector: 'app-close-survey-button',
    templateUrl: './close-survey-button.component.html',
    styleUrls: ['./close-survey-button.component.sass']
})
export class CloseSurveyButtonComponent implements OnInit {

    @Input() surveyId = 0;
    @Input() timeSlot: TimeslotDto = { id: 0, start: "", end: ""};

    constructor(public dialog: MatDialog) {
    }

    ngOnInit(): void {
    }

    openDialog(): void {
        this.dialog.open(CloseSurveyComponent, {
            width: '50%',
            data: { surveyId: this.surveyId, timeslot: this.timeSlot }
        });
    }
}
