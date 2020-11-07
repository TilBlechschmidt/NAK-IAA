import {OnInit} from '@angular/core';
import {SurveysService} from '../../../api/services/surveys.service';
import {QuerySurveysResult} from '../../../api/models/query-surveys-result';

export class AbstractSurveyTableComponent implements OnInit {

    constructor(private service: SurveysService,
                private isCompleted: QueryParameterToggle,
                private isUpcoming: QueryParameterToggle,
                private didParticipateIn: QueryParameterToggle,
                private requiresAttention: QueryParameterToggle,
                private isOwnSurvey: QueryParameterToggle) {}

    data: QuerySurveysResult[] = [];
    fetchError = false;

    displayedColumns: string[] = ['title', 'participantCount'];

    ngOnInit(): void {
        this.service.querySurveys({
            isCompleted: this.isCompleted,
            isUpcoming: this.isUpcoming,
            didParticipateIn: this.didParticipateIn,
            isOwnSurvey: this.isOwnSurvey,
            requiresAttention: this.requiresAttention
        }).subscribe(
            next => this.data = next.surveys, error => this.fetchError = false
        );
    }
}

type QueryParameterToggle = boolean | undefined;
