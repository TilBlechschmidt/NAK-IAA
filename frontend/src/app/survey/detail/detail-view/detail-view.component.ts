import {Component, Input, OnInit} from '@angular/core';
import {SurveysService} from '../../../api/services/surveys.service';
import {ActivatedRoute, Router} from '@angular/router';
import {MatDialog} from '@angular/material/dialog';
import {DeleteSurveyComponent} from '../delete-survey/delete-survey.component';
import {Identifier, ResponseDto, ResponseValueDto, TimeslotDto} from '../../../api/models';
import {EditSurveyWarnComponent} from '../edit-view-warn/edit-survey-warn.component';
import {ResponsesService} from '../../../api/services/responses.service';
import {Mode} from '../response/response.component';

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
    timeSlots: TimeslotDto[] = [];
    responses: ResponseValueDto[] = [];
    saveError = false;
    fetchError = false;
    routerError = false;
    isDeletable = false;
    myResponse?: ResponseDto;
    id?: Identifier;

    constructor(private service: SurveysService, private responseService: ResponsesService, private router: Router,
                private deleteDialog: MatDialog, private editDialog: MatDialog, private route: ActivatedRoute) {
        this.route.params.subscribe(next => this.id = next.id);
    }

    ngOnInit(): void {
        if (this.id === undefined) {
            this.routerError = true;
        } else {
            this.service.querySurvey({id: this.id}).subscribe(next => {
                this.title = next.title;
                this.description = next.description;
                this.timeSlots = next.timeslots;
                this.isEditable = next.isEditable;
                this.isDeletable = next.isDeletable;
                this.myResponse = next.myResponse;
            }, error => this.fetchError = true);
        }
    }

    toggleEdit(): void {
        this.isEdit = !this.isEdit;
    }

    getMode(): Mode {
        return !this.isEditable ? 'view' : this.isEdit ? 'edit' : 'view-not-answerable';
    }

    private haveTimeslotsChanged(): boolean {
        // Not implemented
        return true;
    }

    submit(): void {
        if (this.isEditable) {
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
        } else {
            this.respondSurvey();
        }
    }

    updateSurvey(): void {
        if (this.id === undefined) {
            this.routerError = true;
        } else {
            this.service.updateSurvey({
                id: this.id,
                body: {
                    title: this.title,
                    description: this.description,
                    timeslots: this.timeSlots,
                }
            }).subscribe(next => this.router.navigateByUrl('/survey'),
                error => this.saveError = true);
        }
    }

    respondSurvey(): void {
        if (this.id === undefined) { return; }

        if (this.myResponse) {
            this.responseService.updateResponse({
                responseID: this.myResponse.responseID,
                surveyID: this.id, body: {
                    values: this.responses
                }
            }).subscribe(next => this.router.navigateByUrl('survey'));
        } else {
            this.responseService.createResponse({
                surveyID: this.id, body: {
                    values: this.responses
                }
            }).subscribe(next => this.router.navigateByUrl('survey'));
        }
    }

    delete(): void {
        const dialogRef = this.deleteDialog.open(DeleteSurveyComponent, {
            width: '400px',
            data: {id: this.id, title: this.title}
        });

        dialogRef.afterClosed().subscribe(result => {
            this.title = result.title;
            this.description = result.description;
        });
    }

    updateTimeSlot(index: number, updatedTimeslot: TimeslotDto): void {
        this.timeSlots[index] = updatedTimeslot;
    }

    onDelete(index: number): void {
        this.timeSlots.splice(index, 1);
    }

    responded(response: ResponseValueDto): void {
        this.responses.push(response);
    }
}
