import {Injectable} from '@angular/core';
import format from 'date-fns/format';
import formatISO from 'date-fns/formatISO';
import parse from 'date-fns/parse';
import parseISO from 'date-fns/parseISO';
import isValid from 'date-fns/isValid';
import isBefore from 'date-fns/isBefore';
import {TimeslotDto} from './api/models';
import isEqual from 'date-fns/isEqual';

export type ISODateString = string;
export type HumanReadableDateString = string;

@Injectable({
    providedIn: 'root'
})
export class DateService {
    private static readonly humanReadableFormat = 'yyyy-MM-dd HH:mm';

    constructor() {}

    parseHumanReadable(date: HumanReadableDateString, referenceDate: Date = new Date()): Date {
        return parse(date, DateService.humanReadableFormat, referenceDate);
    }

    formatHumanReadable(date: ISODateString): HumanReadableDateString {
        return format(parseISO(date), DateService.humanReadableFormat);
    }

    formatISO(date: HumanReadableDateString, referenceDate: Date = new Date()): ISODateString {
        return formatISO(this.parseHumanReadable(date, referenceDate));
    }

    validateHumanReadable(date: HumanReadableDateString, referenceDate: Date = new Date()): boolean {
        return isValid(this.parseHumanReadable(date, referenceDate));
    }

    isBefore(a: HumanReadableDateString, b: HumanReadableDateString): boolean {
        return isBefore(this.parseHumanReadable(a), this.parseHumanReadable(b));
    }

    formatTimeslotRange(timeslot: TimeslotDto): string {
        const start = this.formatHumanReadable(timeslot.start);

        if (timeslot.end !== undefined && timeslot.end !== null) {
            const end = this.formatHumanReadable(timeslot.end);
            return `${start} - ${end}`;
        } else {
            return start;
        }
    }

    isEqual(a: HumanReadableDateString, b: HumanReadableDateString): boolean {
        return isEqual(this.parseHumanReadable(a), this.parseHumanReadable(b));
    }

}
