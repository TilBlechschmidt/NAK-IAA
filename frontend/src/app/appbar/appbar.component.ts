import { Component, OnInit } from '@angular/core';
import {TokenService} from "../authentication/service/token.service";

@Component({
  selector: 'app-appbar',
  templateUrl: './appbar.component.html',
  styleUrls: ['./appbar.component.sass']
})
export class AppbarComponent implements OnInit {

  constructor(private authService: TokenService) { }

  ngOnInit(): void {
  }

  changeLanguage(code: string) {
      localStorage.setItem('locale', code);
      window.location.reload();
  }

  isAuthenticated() {
      return this.authService.isAuthenticated();
  }

}
