import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {Mode} from '../response/response.component';
import {ResponseValueDto} from '../../../api/models/response-value-dto';
import {TimeslotDto} from '../../../api/models/timeslot-dto';
import {TimeslotCreationDto} from '../../../api/models/timeslot-creation-dto';

@Component({
    selector: 'app-response-overview',
    templateUrl: './response-overview.component.html',
    styleUrls: ['./response-overview.component.sass']
})
export class ResponseOverviewComponent implements OnInit {

    @Input() timeslots: TimeslotDto[] = [];
    @Output() timeSlotsChanged = new EventEmitter<TimeslotDto[]>();
    @Output() respondChanged = new EventEmitter<ResponseValueDto>();
    mode: Mode = 'view';

    constructor() {
    }

    ngOnInit(): void {
    }


}
