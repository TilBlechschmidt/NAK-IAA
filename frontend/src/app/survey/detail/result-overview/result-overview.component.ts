import {Component, Input, OnChanges, OnInit, SimpleChanges} from '@angular/core';
import {TimeslotDto} from '../../../api/models/timeslot-dto';
import {ResponseDto} from '../../../api/models/response-dto';
import * as moment from 'moment';
import {Identifier} from '../../../api/models';

@Component({
    selector: 'app-result-overview',
    templateUrl: './result-overview.component.html',
    styleUrls: ['./result-overview.component.sass']
})
export class ResultOverviewComponent implements OnInit, OnChanges {

    @Input() timeSlots: TimeslotDto[] = [];
    @Input() responses: ResponseDto[] = [];
    @Input() surveyId?: Identifier;
    @Input() isCloseable?: boolean;
    displayedColumns: string[] = [];

    constructor() { }

    ngOnInit(): void { }

    responseForParticipantAndTimeslot(participant: ResponseDto, timeslot: TimeslotDto): boolean | null {
        const result = participant.responses.find(r => r.timeslotID === timeslot.id);
        return result ? result.value : null;
    }


    formatDate(date: string | undefined): string {
        if (!date) {
            return '';
        }
        return moment(date).format('yyyy-MM-DD hh:mm');
    }

    ngOnChanges(changes: SimpleChanges): void {
        this.displayedColumns = ['name', ...this.timeSlots.map(timeslot => timeslot.id.toString())];
    }
}
