import {Component, OnInit} from '@angular/core';
import {SurveysService} from '../../../api/services/surveys.service';
import {Router} from '@angular/router';
import {MatDialog} from '@angular/material/dialog';
import {DeleteSurveyComponent} from '../delete-survey/delete-survey.component';
import {TokenService} from '../../../authentication/service/token.service';

@Component({
    selector: 'app-detail-view',
    templateUrl: './detail-view.component.html',
    styleUrls: ['./detail-view.component.sass']
})
export class DetailViewComponent implements OnInit {

    isEdit = false;
    title = '';
    description = '';
    saveError = false;

    constructor(public service: SurveysService, public router: Router, public dialog: MatDialog, private tokenService: TokenService) {
    }

    ngOnInit(): void {
    }

    toggleEdit(): void {
        this.isEdit = !this.isEdit;
    }

    submit(): void {
        this.service.createSurvey({
            body: {
                creator: {id: 0, name: ''},
                title: this.title,
                description: this.description,
                timeslots: [],
                selectedTimeslot: undefined,
                isClosed: false
            }
        }).subscribe(next => this.router.navigateByUrl('/survey'),
            error => this.saveError = true);
    }

    delete(): void {
        const dialogRef = this.dialog.open(DeleteSurveyComponent, {
            width: '400px',
            data: {title: this.title, description: this.description}
        });

        dialogRef.afterClosed().subscribe(result => {
            this.title = result.title;
            this.description = result.description;
        });
    }

}
