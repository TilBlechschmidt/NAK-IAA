import {Component, Input, OnInit} from '@angular/core';
import {QuerySurveysResult} from '../../../api/models/query-surveys-result';
import {SurveysService} from '../../../api/services/surveys.service';

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

    constructor(protected surveysService: SurveysService) {}

    ngOnInit(): void {
        this.surveysService.querySurveys({
            isCompleted: this.filter.isCompleted,
            isUpcoming: this.filter.isUpcoming,
            didParticipateIn: this.filter.didParticipateIn,
            isOwnSurvey: this.filter.isOwnSurvey,
            requiresAttention: this.filter.requiresAttention
        }).subscribe(next => this.data = next.surveys, error => {
            this.fetchError = true;
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
