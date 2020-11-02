import {Component, Input, OnInit} from '@angular/core';
import {QuerySurveysResult} from '../../../api/models/query-surveys-result';
import {SurveysService} from '../../../api/services/surveys.service';
import {TokenService} from '../../../authentication/service/token.service';

@Component({
    selector: 'app-abstract-survey-table',
    templateUrl: './abstract-survey-table.component.html',
    styleUrls: ['./abstract-survey-table.component.sass']
})
export class AbstractSurveyTableComponent implements OnInit {

    @Input()
    filter: Filter = {};

    displayedColumns: string[] = ['title', 'participantCount'];
    data: QuerySurveysResult[] = [];
    fetchError = false;

    mockData: QuerySurveysResult[] =
        [{id: 1, title: 'Fest', participantCount: 4},
         {id: 2, title: 'Fest2', participantCount: 6},
         {id: 3, title: 'Fest3', participantCount: 8}];

    constructor(protected surveysService: SurveysService, protected tokenService: TokenService) {}

    ngOnInit(): void {
        this.surveysService.querySurveys({
            isCompleted: this.filter.isCompleted,
            isUpcoming: this.filter.isUpcoming,
            didParticipateIn: this.filter.didParticipateIn,
            isOwnSurvey: this.filter.isOwnSurvey,
            requiresAttention: this.filter.requiresAttention,
            Authorization: this.tokenService.getToken()
        }).subscribe(next => next.surveys, error => {
            this.fetchError = true;
            this.data = this.mockData;
        });
    }
}


export interface Filter {
    isCompleted?: boolean;
    isUpcoming?: boolean;
    didParticipateIn?: boolean;
    requiresAttention?: boolean;
    isOwnSurvey?: boolean;
}
