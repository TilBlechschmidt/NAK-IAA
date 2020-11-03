import { Component, OnInit } from '@angular/core';
import {AuthenticatedResponse, EMail, Password} from '../../api/models';
import {Router} from '@angular/router';
import {TokenService} from '../service/token.service';
import {AccountService} from '../../api/services/account.service';


@Component({
  selector: 'app-authentication-dialog',
  templateUrl: './authentication-dialog.component.html',
  styleUrls: ['./authentication-dialog.component.sass']
})
export class AuthenticationDialogComponent implements OnInit {

   email: EMail =  '';
   password: Password = '';
   authError = false;

  constructor(private accountService: AccountService, private authService: TokenService, private router: Router) { }

  ngOnInit(): void {
  }

  signIn(): void {
      this.accountService.authenticate({body: {email: this.email, password: this.password}}).subscribe(
          (next: AuthenticatedResponse) => {
              this.authService.setToken(next.token);
              this.router.navigateByUrl('survey');
          },  (err: AuthenticatedResponse) => this.authError = true
      );
  }

}
