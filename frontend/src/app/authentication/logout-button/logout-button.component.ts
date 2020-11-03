import { Component, OnInit } from '@angular/core';
import {Router} from '@angular/router';
import {TokenService} from '../service/token.service';

@Component({
  selector: 'app-logout-button',
  templateUrl: './logout-button.component.html',
  styleUrls: ['./logout-button.component.sass']
})
export class LogoutButtonComponent implements OnInit {

  constructor(private router: Router, private authService: TokenService) { }

  ngOnInit(): void {}

  logout(): void {
      this.authService.deleteToken();
      this.router.navigateByUrl('sign_in');
  }

}
