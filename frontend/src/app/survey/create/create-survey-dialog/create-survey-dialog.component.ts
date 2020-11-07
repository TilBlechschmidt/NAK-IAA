import {Component, OnInit} from '@angular/core';
import {MatDialogRef} from '@angular/material/dialog';
import {SurveysService} from '../../../api/services/surveys.service';

@Component({
    selector: 'app-create-survey-dialog',
    templateUrl: './create-survey-dialog.component.html',
    styleUrls: ['./create-survey-dialog.component.sass']
})
export class CreateSurveyDialogComponent implements OnInit {

    title = '';
    description = '';
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
                timeslots: []
            }
        }).subscribe(next => this.onNoClick(), err  => this.saveError = true);
    }
}
