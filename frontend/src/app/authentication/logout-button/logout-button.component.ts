import { Component, OnInit } from '@angular/core';
import {Router} from '@angular/router';
import {TokenService} from '../service/token.service';
import {MatSnackBar} from '@angular/material/snack-bar';
import {TranslateService} from '@ngx-translate/core';

@Component({
  selector: 'app-logout-button',
  templateUrl: './logout-button.component.html',
  styleUrls: ['./logout-button.component.sass']
})
export class LogoutButtonComponent implements OnInit {

  constructor(private router: Router, private authService: TokenService, private snackBar: MatSnackBar,
              private translateService: TranslateService) { }

  ngOnInit(): void {}

  logout(): void {
      this.authService.deleteToken();
      this.router.navigateByUrl('sign_in');
      this.translateService.get('auth.logged_out').subscribe(message => {
          this.snackBar.open(message);
      });
  }

}
