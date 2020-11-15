import {Component, EventEmitter, Injectable, Input, OnChanges, OnInit, Output, SimpleChanges} from '@angular/core';
import {
    AbstractControl,
    FormBuilder,
    FormControl,
    FormGroup,
    ValidationErrors,
    ValidatorFn,
    Validators
} from '@angular/forms';
import {ResponseValueDto} from '../../../api/models/response-value-dto';
import {DateService} from '../../../date.service';
import {TimeslotBo} from '../detail-view/detail-view.component';

@Component({
    selector: 'app-response',
    templateUrl: './response.component.html',
    styleUrls: ['./response.component.sass']
})
export class ResponseComponent implements OnInit, OnChanges {
    form: FormGroup;
    isNew = false;
    formError = false;
    date: Date = new Date();
    state: State = 'neutral';
    @Input() timeSlot?: TimeslotBo;
    @Input() response?: ResponseValueDto;
    @Input() mode: Mode = 'view';
    @Output() timeslotChanged = new EventEmitter<TimeslotBo>();
    @Output() deleted = new EventEmitter();
    @Output() responded = new EventEmitter<ResponseValueDto>();

    constructor(private formBuilder: FormBuilder, private dateService: DateService, private dateValidatorService: DateValidatorService) {
        this.form = this.formBuilder.group({
            start: new FormControl('', [dateValidatorService.validate(), Validators.required]),
            end: new FormControl('', [dateValidatorService.validate()])
        });
    }

    ngOnChanges(changes: SimpleChanges): void {
        if (this.response !== undefined) {
            if (this.response.value) {
                this.state = 'accept';
            } else {
                this.state = 'reject';
            }
        }
    }

    ngOnInit(): void {
        this.form.controls.start.setValue(this.timeSlot?.start);
        this.form.controls.end.setValue(this.timeSlot?.end);
    }

    isEndPresent(): boolean {
        return this.form.get('end')?.value !== '';
    }

    onBlur(): void {
        if (this.isInMode(['edit'])) {
            this.onChange();
        }
    }

    onChange(): void {
        if (!this.isFormValid()) {
            this.formError = true;
        } else {
            this.formError = false;
            const start: string = this.form.get('start')?.value;
            const end: string = this.form.get('end')?.value;
            this.timeslotChanged.emit({
                id: this.timeSlot?.id,
                start,
                end
            });
            this.form.controls.start.setValue('');
            this.form.controls.end.setValue('');
        }
    }

    private isFormValid(): boolean {
        return !this.form.get('start')?.invalid && !this.form.get('end')?.invalid;
    }

    isInMode(modes: Mode[]): boolean {
        return modes.some(mode => this.mode === mode);
    }

    setResponse(response: boolean): void {
        if (response) {
            this.state = 'accept';
        } else {
            this.state = 'reject';
        }

        if (this.timeSlot && this.timeSlot.id) {
            this.responded.emit({timeslotID: this.timeSlot?.id, value: response});
        }
    }

    onDelete(): void {
        this.deleted.emit();
    }

    update(control: string, value: Event): void {
        return this.form.controls[control].setValue(value);
    }
}

export type Mode = 'view' | 'edit' | 'create' | 'create-view' | 'view-not-answerable';

export type State = 'accept' | 'reject' | 'neutral';

@Injectable({
    providedIn: 'root'
})
export class DateValidatorService {
    constructor(private dateService: DateService) {
    }

    validate(): ValidatorFn {
        return (AC: AbstractControl): ValidationErrors | null => {
            if (AC && AC.value && !this.dateService.validateHumanReadable(AC.value)) {
                return {dateValidator: true};
            }
            return null;
        };
    }
}

