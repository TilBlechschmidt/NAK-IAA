import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material/dialog';

@Component({
  selector: 'app-create-survey-dialog',
  templateUrl: './create-survey-dialog.component.html',
  styleUrls: ['./create-survey-dialog.component.sass']
})
export class CreateSurveyDialogComponent implements OnInit {

    constructor(
        public dialogRef: MatDialogRef<CreateSurveyDialogComponent>,
        @Inject(MAT_DIALOG_DATA) public data: DialogData) {}

    onNoClick(): void {
        this.dialogRef.close(this.data);
    }

    ngOnInit(): void {
    }
}

export interface DialogData {
    title: string;
    description: string;
}
