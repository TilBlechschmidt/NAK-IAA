import {Component, Input, OnInit} from '@angular/core';
import {QuerySurveysResult} from '../../../api/models/query-surveys-result';
import {SurveysService} from '../../../api/services/surveys.service';
import {DateService} from '../../../date.service';
import {TimeslotDto} from '../../../api/models/timeslot-dto';

@Component({
    selector: 'app-abstract-survey-table',
    templateUrl: './abstract-survey-table.component.html',
    styleUrls: ['./abstract-survey-table.component.sass']
})
export class AbstractSurveyTableComponent implements OnInit {

    @Input()
    filter: Filter = {};

    displayedColumns: string[] = [];
    data: QuerySurveysResult[] = [];
    fetchError = false;

    constructor(protected surveysService: SurveysService, private dateService: DateService) {
    }

    ngOnInit(): void {
        this.surveysService.querySurveys({
            isClosed: this.filter.isClosed,
            isUpcoming: this.filter.isUpcoming,
            didParticipateIn: this.filter.didParticipateIn,
            isOwnSurvey: this.filter.isOwnSurvey,
            requiresAttention: this.filter.requiresAttention,
            acceptsSelectedTimeslot: this.filter.acceptsSelectedTimeslot
        }).subscribe(next => {
            this.data = next.surveys;
            this.displayedColumns = this.filter.acceptsSelectedTimeslot ?
                ['title', 'participantCount', 'selectedTimeslot'] : ['title', 'participantCount'];
        }, error => {
            this.fetchError = true;
        });
    }

    formatDate(selectedTimeslot: TimeslotDto): string {
        const start = this.dateService.formatHumanReadable(selectedTimeslot.start);
        if (selectedTimeslot.end) {
            return start + ' - ' + this.dateService.formatHumanReadable(selectedTimeslot.end);
        } else {
            return start;
        }
    }
}


export interface Filter {
    isClosed?: boolean;
    isUpcoming?: boolean;
    didParticipateIn?: boolean;
    requiresAttention?: boolean;
    isOwnSurvey?: boolean;
    acceptsSelectedTimeslot?: boolean;
}
