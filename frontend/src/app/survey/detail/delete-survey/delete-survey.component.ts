import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material/dialog';
import {DetailViewComponent} from '../detail-view/detail-view.component';
import {SurveysService} from '../../../api/services/surveys.service';
import {Identifier} from '../../../api/models';
import {Router} from '@angular/router';

@Component({
    selector: 'app-delete-survey',
    templateUrl: './delete-survey.component.html',
    styleUrls: ['./delete-survey.component.sass']
})
export class DeleteSurveyComponent implements OnInit {

    deleteError = false;

    constructor(public dialogRef: MatDialogRef<DetailViewComponent>,
                @Inject(MAT_DIALOG_DATA) public data: DialogData,
                public service: SurveysService, public router: Router) {
    }

    ngOnInit(): void {
    }

    onNoClick(): void {
        this.dialogRef.close();
    }

    delete(): void {
        this.service.deleteSurvey({
            id: this.data.id
        }).subscribe(next => {this.router.navigateByUrl('survey'); this.dialogRef.close(); }, error => this.deleteError = true);
    }
}

interface DialogData {
    id: Identifier;
    title: string;
}
