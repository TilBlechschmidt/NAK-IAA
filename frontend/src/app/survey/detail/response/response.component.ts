import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
import {TimeslotCreationDto} from '../../../api/models/timeslot-creation-dto';
import {ResponseValueDto} from '../../../api/models/response-value-dto';
import {TimeslotDto} from '../../../api/models/timeslot-dto';

@Component({
    selector: 'app-response',
    templateUrl: './response.component.html',
    styleUrls: ['./response.component.sass']
})
export class ResponseComponent implements OnInit {
    form: FormGroup;
    isNew = false;
    date: Date = new Date();
    @Input() timeSlot: TimeslotDto = {id: 0, start: '', end: ''};
    @Input() timeSlotCreate: TimeslotCreationDto = {start: '', end: ''};
    @Input() mode: Mode = 'view';
    @Output() responseEdited = new EventEmitter<TimeslotDto>();
    @Output() timeslotCreated = new EventEmitter<TimeslotCreationDto>();
    @Output() timeslotUpdated = new EventEmitter<TimeslotCreationDto>();
    @Output() deleted = new EventEmitter();
    @Output() responded = new EventEmitter<ResponseValueDto>();

    constructor(private formBuilder: FormBuilder) {
        const dateRegex = '^[0-9]{4}\\-((0[1-9]|1[0-2])){1}-(0[1-9]|1[0-9]|2[0-9]|3[0-1])\\' +
            ' (23\\:59\\:60|(([01]([0-9])|(2[0-3])){1}:?([0-5][0-9])){1})$';

        this.form = this.formBuilder.group({
            start: new FormControl('', [Validators.required, Validators.pattern(dateRegex)]),
            end: new FormControl('', [Validators.pattern(dateRegex)])
        });
    }

    ngOnInit(): void {
        if (this.timeSlot.id) {
            this.form.controls.start.setValue(this.dateToString(this.timeSlot.start));
            this.form.controls.end.setValue(this.dateToString(this.timeSlot.end));
        } else {
            this.form.controls.start.setValue(this.dateToString(this.timeSlotCreate.start));
            this.form.controls.end.setValue(this.dateToString(this.timeSlotCreate.end));
        }
    }

    stringToDate(str: string): string {
        if (str === '') {
            return '';
        }
        const splitString = str.split(' ');
        return splitString[0] + 'T' + splitString[1] + 'Z';
    }

    dateToString(date: string): string {
        if (date === null || date === '' || date === undefined) {
            return '';
        }
        const d1 = date.replace('T', ' ');
        const d2 = d1.replace('Z', '');
        const splitDate = d2.split(':');
        return splitDate[0] + ':' + splitDate[1];
    }

    isEndPresent(): boolean {
        return this.form.get('end')?.value !== '';
    }

    onChange(): void {
        const start: string = this.form.get('start')?.value;
        const end: string = this.form.get('end')?.value;
        if (this.isFormValid()) {
            this.responseEdited.emit({
                id: this.timeSlot.id,
                start: this.stringToDate(start),
                end: this.stringToDate(end)
            });
            this.timeslotCreated.emit(
                {
                    start: this.stringToDate(start),
                    end: this.stringToDate(end)
                }
            );
            this.form.controls.start.setValue('');
            this.form.controls.end.setValue('');
        }
    }

    isInMode(modes: Mode[]): boolean {
        return modes.some(mode => this.mode === mode);
    }

    isFormValid(): boolean {
        return this.form.get('date') !== undefined &&
            this.form.get('start') !== undefined &&
            this.form.get('end') !== undefined;
    }

    setResponse(response: boolean): void {
        this.responded.emit({timeslotID: this.timeSlot.id, value: response});
    }

    onDelete(): void {
        this.deleted.emit();
    }

    update(control: string, value: Event): void {
        return this.form.controls[control].setValue(value);
    }
}

export type Mode = 'view' | 'edit' | 'create' | 'create-view' | 'view-not-answerable';

