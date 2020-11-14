import {Component, Input, OnInit} from '@angular/core';
import {MatDialog} from '@angular/material/dialog';
import {CloseSurveyComponent} from '../close-survey.component';
import {TimeslotDto} from '../../../api/models/timeslot-dto';
import {Identifier} from '../../../api/models';

@Component({
    selector: 'app-close-survey-button',
    templateUrl: './close-survey-button.component.html',
    styleUrls: ['./close-survey-button.component.sass']
})
export class CloseSurveyButtonComponent implements OnInit {

    @Input() surveyId?: Identifier;
    @Input() timeSlot?: TimeslotDto;

    constructor(public dialog: MatDialog) {
    }

    ngOnInit(): void {
    }

    openDialog(): void {
        this.dialog.open(CloseSurveyComponent, {
            width: '30%',
            data: { surveyId: this.surveyId, timeslot: this.timeSlot }
        });
    }
}

export interface CloseSurveyDialogData {
    surveyId: Identifier;
    timeslot: TimeslotDto;
}
