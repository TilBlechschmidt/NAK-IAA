import {Injectable} from '@angular/core';
import format from 'date-fns/format';
import formatISO from 'date-fns/formatISO';
import parse from 'date-fns/parse';
import parseISO from 'date-fns/parseISO';
import isValid from 'date-fns/isValid';

export type ISODateString = string;
export type HumanReadableDateString = string;

@Injectable({
    providedIn: 'root'
})
export class DateService {
    private static readonly humanReadableFormat = 'yyyy-MM-dd HH:mm';

    constructor() {}

    parseHumanReadable(date: HumanReadableDateString, referenceDate: Date): Date {
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
}
