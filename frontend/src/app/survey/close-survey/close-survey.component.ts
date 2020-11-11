import {Component, Inject, OnInit} from '@angular/core';
import {SurveysService} from '../../api/services/surveys.service';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material/dialog';
import {DetailViewComponent} from '../detail/detail-view/detail-view.component';
import {Router} from '@angular/router';
import {TimeslotDto} from '../../api/models/timeslot-dto';

@Component({
    selector: 'app-close-survey',
    templateUrl: './close-survey.component.html',
    styleUrls: ['./close-survey.component.sass']
})
export class CloseSurveyComponent implements OnInit {

    selectedTimeslot = 0;

    constructor(public dialogRef: MatDialogRef<DetailViewComponent>,
                @Inject(MAT_DIALOG_DATA) public surveyId: number,
                @Inject(MAT_DIALOG_DATA) public timeslot: TimeslotDto,
                public service: SurveysService, public router: Router) {
    }

    ngOnInit(): void {

    }

    selectTimeSlot(selectedTimSlot: number) {
        this.selectedTimeslot = selectedTimSlot;
    }

    onNoClick(): void {
        this.dialogRef.close();
    }

    closeSurvey() {
        this.service.closeSurvey({id: this.surveyId, body: {operation: 'close', selectedTimeslot: this.selectedTimeslot}})
            .subscribe(next => this.router.navigateByUrl('surveys?selectedIndex=1'));
    }

}
