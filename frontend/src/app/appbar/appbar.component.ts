import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-appbar',
  templateUrl: './appbar.component.html',
  styleUrls: ['./appbar.component.sass']
})
export class AppbarComponent implements OnInit {

  constructor() { }

  ngOnInit(): void {
  }

  changeLanguage(code: string) {
      localStorage.setItem('locale', code);
      window.location.reload();
  }

}
