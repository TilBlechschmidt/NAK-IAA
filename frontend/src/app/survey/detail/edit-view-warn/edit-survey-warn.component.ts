import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material/dialog';
import {DetailViewComponent} from '../detail-view/detail-view.component';
import {SurveysService} from '../../../api/services/surveys.service';
import {Router} from '@angular/router';

/**
 * @author Hendrik Reiter
 */

@Component({
  selector: 'app-edit-survey-warn',
  templateUrl: './edit-survey-warn.component.html',
  styleUrls: ['./edit-survey-warn.component.sass']
})
export class EditSurveyWarnComponent implements OnInit {

    constructor(public dialogRef: MatDialogRef<DetailViewComponent>,
                public service: SurveysService, public router: Router) {
    }

    ngOnInit(): void {}

    onNoClick(): void {
        this.dialogRef.close(false);
    }

    edit(): void {
        this.dialogRef.close(true);
    }
}

