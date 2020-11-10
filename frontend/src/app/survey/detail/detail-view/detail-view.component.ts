import {Component, Input, OnInit} from '@angular/core';
import {SurveysService} from '../../../api/services/surveys.service';
import {ActivatedRoute, Router} from '@angular/router';
import {MatDialog} from '@angular/material/dialog';
import {DeleteSurveyComponent} from '../delete-survey/delete-survey.component';
import {Identifier, ResponseDto, ResponseValueDto, TimeslotDto} from '../../../api/models';
import {EditSurveyWarnComponent} from '../edit-view-warn/edit-survey-warn.component';
import {ResponsesService} from '../../../api/services/responses.service';
import {Mode} from '../response/response.component';
import * as moment from 'moment';

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
    fetchedTimeSlots: TimeslotDto[] = [];
    fetchedResponses: ResponseDto[] = [];
    saveError = false;
    fetchError = false;
    routerError = false;
    isDeletable = false;
    isClosed = false;
    isClosable = false;
    selectedTimeslot?: TimeslotDto;
    initialTimeSlots: TimeslotDto[] = [];
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
                this.fetchedTimeSlots = next.timeslots;
                this.fetchedResponses = next.responses;
                this.title = next.title;
                this.description = next.description;
                this.timeSlots = next.timeslots.map(t => this.convertTimeSlotToYYYYMMDD(t));
                this.initialTimeSlots = Object.assign([], next.timeslots.map(t => this.convertTimeSlotToYYYYMMDD(t)));
                this.isEditable = next.isEditable;
                this.isDeletable = next.isDeletable;
                this.isClosed = next.isClosed;
                this.isClosable = next.isClosable;
                this.myResponse = next.myResponse;
                this.selectedTimeslot = next.selectedTimeslot;
            }, error => this.fetchError = true);
        }
    }

    toggleEdit(): void {
        this.isEdit = !this.isEdit;
    }

    getMode(): Mode {
        if (!this.isEditable) {
            return 'view';
        } else {
            if (this.isEdit) {
                return 'edit';
            } else {
                return 'view-not-answerable';
            }
        }
    }

    private haveTimeslotsChanged(): boolean {
        return this.timeSlots.filter(timeslot => !this.includesTimeslot(this.initialTimeSlots, timeslot)).length > 0;
    }

    private includesTimeslot(timeslots: TimeslotDto[], timeSlot: TimeslotDto): boolean {
        return timeslots.filter(t => t.start === timeSlot.start && t.end === timeSlot.end).length > 0;
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
                    timeslots: this.timeSlots.map(timeslot => this.convertTimeSlotToISOString(timeslot)),
                }
            }).subscribe(next => this.router.navigateByUrl('/survey'),
                error => this.saveError = true);
        }
    }

    convertTimeSlotToISOString(timeSlot: TimeslotDto): TimeslotDto {
        return {id: timeSlot.id, start: moment(timeSlot.start).toISOString(), end: moment(timeSlot.end).toISOString()};
    }

    convertTimeSlotToYYYYMMDD(timeSlot: TimeslotDto): TimeslotDto {
        return {id: timeSlot.id, start: this.convertDate(timeSlot.start), end: this.convertDate(timeSlot.end)};
    }

    convertDate(date: string | undefined): string {
        if (!date) { return ''; }
        return moment(date).format('YYYY-MM-DD hh:mm');
    }

    respondSurvey(): void {
        if (this.id === undefined) {
            return;
        }

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
        this.deleteDialog.open(DeleteSurveyComponent, {
            width: '400px',
            data: {id: this.id, title: this.title}
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
