import {Component, Input, OnInit} from '@angular/core';
import {SurveysService} from '../../../api/services/surveys.service';
import {ActivatedRoute, Router} from '@angular/router';
import {MatDialog} from '@angular/material/dialog';
import {DeleteSurveyComponent} from '../delete-survey/delete-survey.component';
import {Identifier} from '../../../api/models';
import {TokenService} from '../../../authentication/service/token.service';
import {EditSurveyWarnComponent} from '../edit-view/edit-survey-warn.component';

@Component({
    selector: 'app-detail-view',
    templateUrl: './detail-view.component.html',
    styleUrls: ['./detail-view.component.sass']
})
export class DetailViewComponent implements OnInit {

    @Input()
    isEditable = false;

    isEdit = false;
    title = '';
    description = '';
    saveError = false;
    fetchError = false;
    id?: Identifier;

    constructor(private service: SurveysService, private router: Router, private deleteDialog: MatDialog, private editDialog: MatDialog, private route: ActivatedRoute, private tokenService: TokenService) {
        this.route.params.subscribe(next => this.id = next.id);
    }

    ngOnInit(): void {
        this.service.querySurvey({id: this.id!, Authorization: this.tokenService.getToken()}).subscribe(next => {
            this.title = next.title;
            this.description = next.description;
        }, error => this.fetchError = true);
    }

    toggleEdit(): void {
        this.isEdit = !this.isEdit;
    }

    private haveTimeslotsChanged(): boolean {
        // Not implemented
        return true;
    }

    submit(): void {
        if (this.haveTimeslotsChanged()) {
            this.editDialog.open(EditSurveyWarnComponent, {
                width: '400px'
            }).afterClosed().subscribe(next => {
                if (next) {
                    this.updateSurvey();
                }
            });
        } else {
            this.updateSurvey();
        }
    }

    updateSurvey() {
        this.service.updateSurvey({
            id: this.id!,
            Authorization: this.tokenService.getToken(),
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


    delete() {
        const dialogRef = this.deleteDialog.open(DeleteSurveyComponent, {
            width: '400px',
            data: {title: this.title, description: this.description}
        });

        dialogRef.afterClosed().subscribe(result => {
            this.title = result.title;
            this.description = result.description;
        });
    }
}
