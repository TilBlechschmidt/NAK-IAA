import {Component, Input, OnChanges, OnInit, SimpleChanges} from '@angular/core';
import {TimeslotDto} from '../../../api/models/timeslot-dto';
import {ResponseDto} from '../../../api/models/response-dto';
import {Identifier} from '../../../api/models';
import {DateService} from '../../../date.service';

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

    constructor(private dateService: DateService) { }

    ngOnInit(): void {
        this.displayedColumns = ['name', ...this.timeSlots.map(timeslot => timeslot.id.toString())];
    }

    responseForParticipantAndTimeslot(participant: ResponseDto, timeslot: TimeslotDto): boolean | null {
        const result = participant.responses.find(r => r.timeslotID === timeslot.id);
        return result ? result.value : null;
    }

    positiveResponseCountForTimeslot(timeslot: TimeslotDto): number {
        return this.responses.reduce((acc, response) => {
            return acc + (
                response.responses.find(timeslotResponse =>
                    timeslotResponse.timeslotID === timeslot.id && timeslotResponse.value
                ) !== undefined ? 1 : 0
            );
        }, 0);
    }

    formatDate(date: string | undefined): string {
        return date ? this.dateService.formatHumanReadable(date) : '';
    }

    ngOnChanges(changes: SimpleChanges): void {
        this.displayedColumns = ['name', ...this.timeSlots.map(timeslot => timeslot.id.toString())];
    }
}
