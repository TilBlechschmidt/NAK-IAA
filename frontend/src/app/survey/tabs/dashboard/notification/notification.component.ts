import {Component, OnInit} from '@angular/core';
import {SurveysService} from '../../../../api/services/surveys.service';
import {Router} from '@angular/router';
import {QuerySurveysResult} from '../../../../api/models';

/**
 * @author Hendrik Reiter
 */

@Component({
    selector: 'app-notification',
    templateUrl: './notification.component.html',
    styleUrls: ['./notification.component.sass']
})
export class NotificationComponent implements OnInit {

    notifications: QuerySurveysResult[] = [];

    constructor(private surveysService: SurveysService, private router: Router) {
    }

    ngOnInit(): void {
        this.surveysService.querySurveys({
            requiresAttention: true,
            isClosed: false
        }).subscribe(next => this.notifications = next.surveys, error => this.notifications = []);
    }

    navigateToSurvey(id: number): void {
        this.router.navigateByUrl('/survey/detail/' + id);
    }
}
