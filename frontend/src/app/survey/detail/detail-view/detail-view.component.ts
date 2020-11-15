import {Component, Input, OnInit} from '@angular/core';
import {Location} from '@angular/common';
import {SurveysService} from '../../../api/services/surveys.service';
import {ActivatedRoute, Router} from '@angular/router';
import {MatDialog} from '@angular/material/dialog';
import {DeleteSurveyComponent} from '../delete-survey/delete-survey.component';
import {Identifier, ResponseDto, ResponseValueDto, TimeslotCreationDto, TimeslotDto} from '../../../api/models';
import {EditSurveyWarnComponent} from '../edit-view-warn/edit-survey-warn.component';
import {ResponsesService} from '../../../api/services/responses.service';
import {Mode} from '../response/response.component';
import {DateService} from '../../../date.service';

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
    timeSlots: TimeslotBo[] = [];
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
    initialTimeSlots: TimeslotBo[] = [];
    creatorName = '';
    myResponse?: ResponseDto;
    id?: Identifier;
    timeSlotsEmptyError = false;
    duplicateError = false;

    constructor(private service: SurveysService, private responseService: ResponsesService,
                private dateService: DateService, private router: Router,
                private deleteDialog: MatDialog, private editDialog: MatDialog, private route: ActivatedRoute,
                private location: Location) {
        this.route.params.subscribe(next => this.id = next.id);
    }

    ngOnInit(): void {
        this.refetchSurvey();
    }

    refetchSurvey(): void {
        if (this.id === undefined) {
            this.routerError = true;
        } else {
            this.service.querySurvey({id: this.id}).subscribe(next => {
                this.fetchedTimeSlots = next.timeslots;
                this.fetchedResponses = next.responses;
                this.title = next.title;
                this.description = next.description;
                this.timeSlots = next.timeslots.map(t => this.convertTimeSlotToHumanReadable(t)).map(t => timeslotDtoToBo(t));
                this.initialTimeSlots = Object.assign([], next.timeslots.map(t => this.convertTimeSlotToHumanReadable(t)));
                this.isEditable = next.isEditable;
                this.isDeletable = next.isDeletable;
                this.isClosed = next.isClosed;
                this.isClosable = next.isClosable;
                this.myResponse = next.myResponse;
                this.selectedTimeslot = next.selectedTimeslot;
                this.creatorName = next.creator.name;
            }, error => this.fetchError = true);
        }
    }

    toggleEdit(): void {
        this.isEdit = !this.isEdit;
    }

    abortEdit(): void {
        this.isEdit = false;
        this.refetchSurvey();
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

    private includesTimeslot(timeslots: TimeslotBo[], timeSlot: TimeslotBo): boolean {
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
        if (this.timeSlots.length < 1) {
            this.timeSlotsEmptyError = true;
            return;
        }

        if (this.id === undefined) {
            this.routerError = true;
        } else {
            this.service.updateSurvey({
                id: this.id,
                body: {
                    title: this.title,
                    description: this.description,
                    timeslots: this.timeSlots.map(timeslot => this.convertTimeSlotToISOString(timeslot))
                        .map(ts => timeslotBoToCreationDto(ts)),
                }
            }).subscribe(next => {
                this.isEdit = false;
                this.initialTimeSlots = this.timeSlots;
                this.refetchSurvey();
            }, error => this.saveError = true);
        }
    }

    convertTimeSlotToISOString(timeSlot: TimeslotBo): TimeslotBo {
        return timeSlot.end ? {
            start: this.dateService.formatISO(timeSlot.start),
            end: this.dateService.formatISO(timeSlot.end)
        } : { start: this.dateService.formatISO(timeSlot.start)};
    }

    convertTimeSlotToHumanReadable(timeSlot: TimeslotDto): TimeslotDto {
        return {
            id: timeSlot.id,
            start: this.convertDateToHumanReadable(timeSlot.start),
            end: this.convertDateToHumanReadable(timeSlot.end)
        };
    }

    formatTimeslotRange(timeslot: TimeslotDto): string {
        return this.dateService.formatTimeslotRange(timeslot);
    }

    convertDateToHumanReadable(date: string | undefined): string {
        return date ? this.dateService.formatHumanReadable(date) : '';
    }

    respondSurvey(): void {
        if (this.id === undefined) {
            return;
        }

        if (this.myResponse) {
            this.responseService.updateResponse({
                responseID: this.myResponse.responseID,
                surveyID: this.id, body: {
                    values: this.mergeResponseWithNewResponses(this.myResponse.responses, this.responses)
                }
            }).subscribe(() => {
                this.responses = [];
                this.refetchSurvey();
            });
        } else {
            this.responseService.createResponse({
                surveyID: this.id, body: {
                    values: this.responses
                }
            }).subscribe(() => {
                this.responses = [];
                this.refetchSurvey();
            }, err => this.saveError = true);
        }
    }

    private mergeResponseWithNewResponses(myResponse: ResponseValueDto[], newResponse: ResponseValueDto[]): ResponseValueDto[] {
        const myResponsesWithoutNewResponses = myResponse.filter(response =>
            !newResponse.map(res => res.timeslotID).includes(response.timeslotID));
        return newResponse.concat(myResponsesWithoutNewResponses);
    }

    delete(): void {
        this.deleteDialog.open(DeleteSurveyComponent, {
            width: '400px',
            data: {id: this.id, title: this.title}
        });
    }

    updateTimeSlot(itemIndex: number, updatedTimeSlot: TimeslotBo): void {
        this.timeSlots[itemIndex] = updatedTimeSlot;
    }

    onDelete(index: number): void {
        this.timeSlots.splice(index, 1);
    }

    responded(response: ResponseValueDto): void {
        const includesResponseNewResponse = this.responses.map(res => res.timeslotID).includes(response.timeslotID);
        if (includesResponseNewResponse) {
            this.responses = this.responses.map(res => {
                if (res.timeslotID === response.timeslotID) {
                    return response;
                } else {
                    return res;
                }
            });
        } else {
            this.responses.push(response);
        }
    }

    getResponse(timeslot: TimeslotBo): ResponseValueDto | undefined {
        return this.myResponse?.responses.find(response => response.timeslotID === timeslot.id);
    }

    back(): void {
        this.location.back();
    }

    notResponded(): boolean {
        return this.getMode() === 'view' && this.responses.length < 1;
    }

    createTimeslot(timeslot: TimeslotBo): void {
        this.duplicateError = false;
        if (this.timeSlots.filter(ts => ts.start === timeslot.start && ts.end === timeslot.end).length > 0) {
            this.duplicateError = true;
            return;
        }
        this.timeSlots.push(timeslot);
    }

}

export interface TimeslotBo {
    id?: Identifier;
    start: string;
    end?: string;
}

export function timeslotDtoToBo(dto: TimeslotDto): TimeslotBo {
    return { id: dto.id, start: dto.start, end: dto.end };
}

export function timeslotCreationDtoToBo(dto: TimeslotCreationDto): TimeslotBo {
    return { start: dto.start, end: dto.end };
}

export function timeslotBoToDto(bo: TimeslotBo): TimeslotDto {
    return { id: bo.id ? bo.id : 0, start: bo.start, end: bo.end ? bo.end : '' };
}

export function timeslotBoToCreationDto(bo: TimeslotBo): TimeslotCreationDto {
    return { start: bo.start, end: bo.end ? bo.end : '' };
}
