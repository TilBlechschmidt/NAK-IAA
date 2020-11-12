import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {AbstractControl, FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
import {TimeslotCreationDto} from '../../../api/models/timeslot-creation-dto';
import {ResponseValueDto} from '../../../api/models/response-value-dto';
import {TimeslotDto} from '../../../api/models/timeslot-dto';
import * as moment from 'moment';


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
        this.form = this.formBuilder.group({
            start: new FormControl('', [DateValidator.validate, Validators.required]),
            end: new FormControl('', [DateValidator.validate])
        });
    }

    ngOnInit(): void {
        if (this.timeSlot.id) {
            this.form.controls.start.setValue(this.timeSlot.start);
            this.form.controls.end.setValue(this.timeSlot.end);
        } else {
            this.form.controls.start.setValue(this.timeSlotCreate.start);
            this.form.controls.end.setValue(this.timeSlotCreate.end);
        }
    }

    isEndPresent(): boolean {
        return this.form.get('end')?.value !== '';
    }

    onChange(): void {
        const start: string = this.form.get('start')?.value;
        const end: string = this.form.get('end')?.value;
        this.responseEdited.emit({
            id: this.timeSlot.id,
            start,
            end
        });
        this.timeslotCreated.emit(
            {
                start,
                end
            }
        );
        this.form.controls.start.setValue('');
        this.form.controls.end.setValue('');
    }

    isInMode(modes: Mode[]): boolean {
        return modes.some(mode => this.mode === mode);
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

export class DateValidator {
    static validate(AC: AbstractControl): {dateValidator: boolean} | null {
        if (AC && AC.value && !moment(AC.value, 'YYYY-MM-DD hh:mm', true).isValid()) {
            return {dateValidator: true};
        }
        return null;
    }
}

