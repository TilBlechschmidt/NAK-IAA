import { Component, OnInit } from '@angular/core';
import {ActivatedRoute, Params} from '@angular/router';

@Component({
  selector: 'app-survey-tab-view',
  templateUrl: './survey-tab-view.component.html',
  styleUrls: ['./survey-tab-view.component.sass']
})
export class SurveyTabViewComponent implements OnInit {

  selectedIndex = 0;

  constructor(private route: ActivatedRoute) { }

  ngOnInit(): void {
      this.route.queryParams.subscribe((params: Params) => this.selectedIndex = params.view);
  }

}
