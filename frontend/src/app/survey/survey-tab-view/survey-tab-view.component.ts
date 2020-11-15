import { Component, OnInit } from '@angular/core';
import {ActivatedRoute, Params, Router} from '@angular/router';

/**
 * @author Hendrik Reiter
 */

@Component({
  selector: 'app-survey-tab-view',
  templateUrl: './survey-tab-view.component.html',
  styleUrls: ['./survey-tab-view.component.sass']
})
export class SurveyTabViewComponent implements OnInit {

  selectedIndex?: number;

  constructor(private route: ActivatedRoute, private router: Router) { }

  ngOnInit(): void {
      this.route.queryParams.subscribe((params: Params) => this.selectedIndex = params.selectedIndex);
  }

  setSelectedIndex(index: number): void {
      this.router.navigateByUrl('survey?selectedIndex=' + index);
  }

}
