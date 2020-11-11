import {Component, Input, OnInit} from '@angular/core';
import {TimeslotDto} from '../../../api/models/timeslot-dto';
import {ResponseDto} from '../../../api/models/response-dto';

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

    appendLeadingZeroes(n: number): string {
        return n <= 9 ? `0${n}` : `${n}`;
    }

    formatDate(dateString: string): string {
        const date = new Date(dateString);
        return `${date.getDate()}.${date.getMonth() + 1}.${date.getFullYear()}`;
    }

    formatTime(dateString: string): string {
        const date = new Date(dateString);
        return `${this.appendLeadingZeroes(date.getHours())}:${this.appendLeadingZeroes(date.getMinutes())}`;
    }
}
