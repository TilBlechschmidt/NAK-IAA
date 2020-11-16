import {Component, OnInit} from '@angular/core';
import {MatDialogRef} from '@angular/material/dialog';
import {SurveysService} from '../../../api/services/surveys.service';
import {TimeslotCreationDto} from '../../../api/models/timeslot-creation-dto';
import {Router} from '@angular/router';
import {DateService} from '../../../date.service';
import {
    TimeslotBo,
    timeslotBoToCreationDto,
    timeslotCreationDtoToBo
} from '../../detail/detail-view/detail-view.component';

@Component({
    selector: 'app-create-survey-dialog',
    templateUrl: './create-survey-dialog.component.html',
    styleUrls: ['./create-survey-dialog.component.sass']
})

/**
 * @author Hendrik Reiter
 */

export class CreateSurveyDialogComponent implements OnInit {

    title = '';
    description = '';
    timeSlots: TimeslotCreationDto[] = [];
    saveError = false;
    duplicateError = false;
    equalsError = false;
    endDateBeforeStartDateError = false;
    titleOrDescriptionTooLongError = false;
    timeSlotsEmptyError = false;
    emptyTitleError = false;

    constructor(
        public dialogRef: MatDialogRef<CreateSurveyDialogComponent>,
        private surveysService: SurveysService, private dateService: DateService, private router: Router) {
    }

    onNoClick(): void {
        this.dialogRef.close();
    }

    ngOnInit(): void {
    }

    submit(): void {
        this.saveError = false;
        this.titleOrDescriptionTooLongError = false;
        this.timeSlotsEmptyError = false;
        this.emptyTitleError = false;

        if (this.title.length > 2048 || this.description.length > 2048) {
            this.titleOrDescriptionTooLongError = true;
        }

        if (this.title === '') {
            this.emptyTitleError = true;
        }

        if (this.timeSlots.length < 1) {
            this.timeSlotsEmptyError = true;
        }

        if (this.timeSlotsEmptyError || this.titleOrDescriptionTooLongError || this.emptyTitleError) {
            return;
        }

        this.surveysService.createSurvey({
            body: {
                title: this.title,
                description: this.description,
                timeslots: this.timeSlots.map(t => this.convertTimeSlotToISOString(t))
            }
        }).subscribe(next => {
            this.onNoClick();
            this.router.navigateByUrl('survey/detail/' + next.id);
        }, ()  => this.saveError = true);
    }

    convertTimeSlotToISOString(timeSlot: TimeslotCreationDto): TimeslotCreationDto {
        return {
            start: this.dateService.formatISO(timeSlot.start),
            end: timeSlot.end ? this.dateService.formatISO(timeSlot.end) : undefined
        };
    }

    createTimeSlot(timeSlot: TimeslotBo): void {
        this.duplicateError = false;
        this.equalsError = false;
        this.endDateBeforeStartDateError = false;

        if (timeSlot.end && this.dateService.isBefore(timeSlot.end, timeSlot.start)) {
            this.endDateBeforeStartDateError = true;
            return;
        }

        if (timeSlot.end && this.dateService.isEqual(timeSlot.start, timeSlot.end)) {
            this.equalsError = true;
            return;
        }

        if (this.timeSlots.filter(ts => ts.start === timeSlot.start && ts.end === timeSlot.end).length > 0) {
            this.duplicateError = true;
            return;
        }

        this.timeSlots.push(timeslotBoToCreationDto(timeSlot));
    }

    onDelete(index: number): void {
        this.timeSlots.splice(index, 1);
    }

    toBo(timeslot: TimeslotCreationDto): TimeslotBo {
        return timeslotCreationDtoToBo(timeslot);
    }
}
