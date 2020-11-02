import {Component, OnInit} from '@angular/core';
import {SurveysService} from '../../../../api/services/surveys.service';
import {TokenService} from '../../../../authentication/service/token.service';
import {Router} from '@angular/router';
import {QuerySurveysResult, Survey} from '../../../../api/models';

@Component({
    selector: 'app-notification',
    templateUrl: './notification.component.html',
    styleUrls: ['./notification.component.sass']
})
export class NotificationComponent implements OnInit {

    notifications: QuerySurveysResult[] = [];

    constructor(private surveysService: SurveysService, private tokenService: TokenService, private router: Router) {
    }

    ngOnInit(): void {
        this.surveysService.querySurveys({
            requiresAttention: true,
            Authorization: this.tokenService.getToken()
        }).subscribe(next => this.notifications = next.surveys, error => this.notifications = []);
    }

    navigateToSurvey(id: number): void {
        this.router.navigateByUrl('/survey/detail/' + id);
    }
}
