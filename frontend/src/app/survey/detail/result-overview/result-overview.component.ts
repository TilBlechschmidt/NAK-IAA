import {Component, Input, OnInit} from '@angular/core';
import {TimeslotDto} from '../../../api/models/timeslot-dto';
import {ResponseDto} from '../../../api/models/response-dto';
import * as moment from 'moment';

@Component({
    selector: 'app-result-overview',
    templateUrl: './result-overview.component.html',
    styleUrls: ['./result-overview.component.sass']
})
export class ResultOverviewComponent implements OnInit {

    @Input() timeSlots: TimeslotDto[] = [];
    @Input() responses: ResponseDto[] = [];
    displayedColumns: string[] = [];

    constructor() {
    }

    ngOnInit(): void {
        setTimeout(() => {
            this.displayedColumns = ['name', ...this.timeSlots.map(timeslot => timeslot.id.toString())];
        }, 500);
    }

    responseForParticipantAndTimeslot(participant: ResponseDto, timeslot: TimeslotDto): boolean | null {
        const result = participant.responses.find(r => r.timeslotID === timeslot.id);
        return result ? result.value : null;
    }

    formatDate(date: string | undefined): string {
        if (!date) { return ''; }
        return moment(date).format('yyyy-MM-DD hh:mm');
    }
}
