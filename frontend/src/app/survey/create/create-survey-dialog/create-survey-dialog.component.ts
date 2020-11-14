import {Component, OnInit} from '@angular/core';
import {MatDialogRef} from '@angular/material/dialog';
import {SurveysService} from '../../../api/services/surveys.service';
import {TimeslotCreationDto} from '../../../api/models/timeslot-creation-dto';
import {TimeslotDto} from '../../../api/models/timeslot-dto';
import * as moment from 'moment';
import {Router} from '@angular/router';

@Component({
    selector: 'app-create-survey-dialog',
    templateUrl: './create-survey-dialog.component.html',
    styleUrls: ['./create-survey-dialog.component.sass']
})
export class CreateSurveyDialogComponent implements OnInit {

    title = '';
    description = '';
    timeSlots: TimeslotCreationDto[] = [];
    saveError = false;
    duplicateError = false;

    constructor(
        public dialogRef: MatDialogRef<CreateSurveyDialogComponent>,
        private surveysService: SurveysService, private router: Router) {
    }

    onNoClick(): void {
        this.dialogRef.close();
    }

    ngOnInit(): void {
    }

    submit(): void {
        this.surveysService.createSurvey({
            body: {
                title: this.title,
                description: this.description,
                timeslots: this.timeSlots.map(t => this.convertTimeSlotToISOString(t))
            }
        }).subscribe(next => {this.onNoClick(), this.router.navigateByUrl('survey/detail/' + next.id); }, err  => this.saveError = true);
    }

    convertTimeSlotToISOString(timeSlot: TimeslotCreationDto): TimeslotCreationDto {
        return { start: moment(timeSlot.start).toISOString(), end: moment(timeSlot.end).toISOString()};
    }

    createTimeSlot(timeSlot: TimeslotCreationDto): void {
        this.duplicateError = false;
        if (this.timeSlots.filter(ts => ts.start === timeSlot.start && ts.start === timeSlot.start).length > 0) {
            this.duplicateError = true;
            return;
        }
        this.timeSlots.push(timeSlot);
    }

    onDelete(index: number): void {
        this.timeSlots.splice(index, 1);
    }
}
