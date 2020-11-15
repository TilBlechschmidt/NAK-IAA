import {Component, Inject, OnInit} from '@angular/core';
import {SurveysService} from '../../api/services/surveys.service';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material/dialog';
import {DetailViewComponent} from '../detail/detail-view/detail-view.component';
import {Router} from '@angular/router';
import {CloseSurveyDialogData} from './close-survey-button/close-survey-button.component';
import {DateService, HumanReadableDateString, ISODateString} from '../../date.service';
import {TimeslotDto} from '../../api/models';

@Component({
    selector: 'app-close-survey',
    templateUrl: './close-survey.component.html',
    styleUrls: ['./close-survey.component.sass']
})
export class CloseSurveyComponent implements OnInit {

    constructor(public dialogRef: MatDialogRef<DetailViewComponent>,
                @Inject(MAT_DIALOG_DATA) public data: CloseSurveyDialogData,
                public service: SurveysService, public dateService: DateService, public router: Router) {
    }

    ngOnInit(): void {
    }

    closeSurvey(): void {
        this.service.closeSurvey({
            id: this.data.surveyId,
            body: {operation: 'close', selectedTimeslot: this.data.timeslot?.id}
        })
            .subscribe(next => {
                this.dialogRef.close();
                this.router.navigateByUrl('survey?selectedIndex=1');
            });
    }

    formatHumanReadable(date: ISODateString): HumanReadableDateString {
        return this.dateService.formatHumanReadable(date);
    }

    formatTimeslotRange(timeslot: TimeslotDto): string {
        return this.dateService.formatTimeslotRange(timeslot);
    }
}
