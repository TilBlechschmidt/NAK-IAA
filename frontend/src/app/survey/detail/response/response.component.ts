import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-response',
  templateUrl: './response.component.html',
  styleUrls: ['./response.component.sass']
})
export class ResponseComponent implements OnInit {
    isEdit = false;
    isNew = false;

    constructor() { }

    ngOnInit(): void {
    }

}
