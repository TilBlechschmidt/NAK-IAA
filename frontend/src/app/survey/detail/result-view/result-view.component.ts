import {Component, OnInit} from '@angular/core';
import {TimeslotDto} from '../../../api/models/timeslot-dto';
import {ResponseDto} from '../../../api/models/response-dto';

@Component({
    selector: 'app-result-view',
    templateUrl: './result-view.component.html',
    styleUrls: ['./result-view.component.sass']
})
export class ResultViewComponent implements OnInit {

    timeslots: TimeslotDto[] = [
        {
            id: 0,
            start: "2020-11-08T14:30:00.000Z",
            end: "2020-11-08T15:30:00.000Z"
        },
        {
            id: 1,
            start: "2020-11-08T15:30:00.000Z",
            end: "2020-11-08T16:30:00.000Z"
        },
        {
            id: 2,
            start: "2020-11-08T16:30:00.000Z",
            end: "2020-11-08T17:30:00.000Z"
        }
    ];

    responses: ResponseDto[] = [
        {
            isEditable: false,
            responseID: 0,
            surveyID: 42,
            user: {
                id: 42,
                name: "Klaus Meier"
            },
            responses: [
                {
                    timeslotID: 0,
                    value: true
                },
                {
                    timeslotID: 1,
                    value: true
                },
                {
                    timeslotID: 2,
                    value: false
                }
            ]
        },
        {
            isEditable: false,
            responseID: 1,
            surveyID: 42,
            user: {
                id: 43,
                name: "Hans Müller"
            },
            responses: [
                {
                    timeslotID: 0,
                    value: false
                },
                {
                    timeslotID: 2,
                    value: true
                }
            ]
        }
    ];

    displayedColumns: string[] = ['name', ...this.timeslots.map(timeslot => timeslot.id.toString())];

    constructor() {
    }

    ngOnInit(): void {
    }

    responseForParticipantAndTimeslot(participant: ResponseDto, timeslot: TimeslotDto): boolean | null {
        const result = participant.responses.find(r => r.timeslotID == timeslot.id);

        return result ? result.value : null;
    }

    appendLeadingZeroes(n: number): string {
        return n<=9 ? `0${n}` : `${n}`;
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
