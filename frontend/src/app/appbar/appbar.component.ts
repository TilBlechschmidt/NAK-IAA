import { Component, OnInit } from '@angular/core';
import {TokenService} from '../authentication/service/token.service';

@Component({
  selector: 'app-appbar',
  templateUrl: './appbar.component.html',
  styleUrls: ['./appbar.component.sass']
})
export class AppbarComponent implements OnInit {

  constructor(private authService: TokenService) { }

  ngOnInit(): void {
  }

  changeLanguage(code: string): void {
      localStorage.setItem('locale', code);
      window.location.reload();
  }

  isAuthenticated(): boolean {
      return this.authService.isAuthenticated();
  }

}
