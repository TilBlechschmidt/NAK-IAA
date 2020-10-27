import { Component, OnInit } from '@angular/core';
import {MatDialog} from "@angular/material/dialog";
import {CreateSurveyDialogComponent} from "../create-survey-dialog/create-survey-dialog.component";

@Component({
  selector: 'app-new-survey-button',
  templateUrl: './new-survey-button.component.html',
  styleUrls: ['./new-survey-button.component.sass']
})
export class NewSurveyButtonComponent implements OnInit {

  title = "";
  description = "";

  constructor(public dialog: MatDialog) { }

  ngOnInit(): void {
  }

  openDialog(): void{
      const dialogRef = this.dialog.open(CreateSurveyDialogComponent, {
          width: '250px',
          data: {title: this.title, description: this.description}
      });

      dialogRef.afterClosed().subscribe(result => {
          console.log('The dialog was closed');
          this.title = result.title;
          this.description = result.description;
      });
  }

}
