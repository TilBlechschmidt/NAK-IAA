import {Component, OnInit} from '@angular/core';
import {SurveysService} from "../../../api/services/surveys.service";
import {Router} from "@angular/router";
import {MatDialog} from "@angular/material/dialog";
import {DeleteSurveyComponent} from "../delete-survey/delete-survey.component";
import {Timeslot} from "../../../api/models";

@Component({
    selector: 'app-detail-view',
    templateUrl: './detail-view.component.html',
    styleUrls: ['./detail-view.component.sass']
})
export class DetailViewComponent implements OnInit {

    isEdit: boolean = false;
    title: string = "";
    description: string = "";
    saveError: boolean = false;

    constructor(public service: SurveysService, public router: Router, public dialog: MatDialog) {
    }

    ngOnInit(): void {
    }

    toggleEdit() {
        this.isEdit = !this.isEdit;
    }

    submit() {
        this.service.createSurvey({
            Authorization: localStorage.getItem("jwt")!,
            body: {
                title: this.title,
                description: this.description,
                timeslots: [],
                selectedTimeslot: undefined,
                isClosed: false
            }
        }).subscribe(next => this.router.navigateByUrl("/survey"),
            error => this.saveError = true);
    }

    delete() {
        const dialogRef = this.dialog.open(DeleteSurveyComponent, {
            width: '400px',
            data: {title: this.title, description: this.description}
        });

        dialogRef.afterClosed().subscribe(result => {
            console.log('The dialog was closed');
            this.title = result.title;
            this.description = result.description;
        });
    }

}
