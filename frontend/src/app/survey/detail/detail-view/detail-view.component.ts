import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-detail-view',
  templateUrl: './detail-view.component.html',
  styleUrls: ['./detail-view.component.sass']
})
export class DetailViewComponent implements OnInit {

   isEdit: boolean = false;
   title: string = "";
   description: string = "";

  constructor() { }

  ngOnInit(): void {
  }

  toggleEdit() {
      this.isEdit = !this.isEdit;
  }

}
