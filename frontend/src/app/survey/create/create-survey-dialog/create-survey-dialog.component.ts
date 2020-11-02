import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material/dialog';
import {SurveysService} from '../../../api/services/surveys.service';
import {TokenService} from '../../../authentication/service/token.service';

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
        private surveysService: SurveysService, private tokenService: TokenService) {
    }

    onNoClick(): void {
        this.dialogRef.close();
    }

    ngOnInit(): void {
    }

    submit() {
        this.surveysService.createSurvey({
            Authorization: this.tokenService.getToken(),
            body: {title: this.title, description: this.description, isClosed: false, timeslots: []}
        }).subscribe(next => this.onNoClick(), err  => this.saveError = true);
    }
}
