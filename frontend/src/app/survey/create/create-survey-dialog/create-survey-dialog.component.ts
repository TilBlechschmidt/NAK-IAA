import {Component, OnInit} from '@angular/core';
import {MatDialogRef} from '@angular/material/dialog';
import {SurveysService} from '../../../api/services/surveys.service';
import {TimeslotCreationDto} from '../../../api/models/timeslot-creation-dto';

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

    constructor(
        public dialogRef: MatDialogRef<CreateSurveyDialogComponent>,
        private surveysService: SurveysService) {
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
                timeslots: this.timeSlots
            }
        }).subscribe(next => {this.onNoClick(), window.location.reload(); }, err  => this.saveError = true);
    }

    createTimeSlot(timeSlot: TimeslotCreationDto): void {
        this.timeSlots.push(timeSlot);
    }

    onDelete(index: number): void {
        this.timeSlots.splice(index, 1);
    }
}
