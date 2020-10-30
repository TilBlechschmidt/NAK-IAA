import {Component} from '@angular/core';
import {SurveysService} from '../../../api/services';
import {TokenService} from '../../../authentication/service/token.service';
import {AbstractSurveyTableComponent} from '../tables/abstractSurveyTable.component';

@Component({
    selector: 'app-fixed-appointment-view',
    templateUrl: './fixed-appointment-view.component.html',
    styleUrls: ['./fixed-appointment-view.component.sass']
})

export class FixedAppointmentViewComponent extends AbstractSurveyTableComponent {

    constructor(private service: SurveysService, private tokenService: TokenService) {
        super(service, tokenService, true, true, undefined, undefined, undefined);
    }

    displayedColumns: string[] = ['title', 'participantCount'];
    dataSource = super.data;

    ngOnInit(): void {
    }
}
