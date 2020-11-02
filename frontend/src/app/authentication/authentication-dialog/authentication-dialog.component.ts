import { Component, OnInit } from '@angular/core';
import {AuthenticationService} from "../../api/services/authentication.service";
import {EMail, Password} from "../../api/models";
import {Router} from "@angular/router";
import {TokenService} from "../service/token.service";


@Component({
  selector: 'app-authentication-dialog',
  templateUrl: './authentication-dialog.component.html',
  styleUrls: ['./authentication-dialog.component.sass']
})
export class AuthenticationDialogComponent implements OnInit {

   email: EMail =  "";
   password: Password = "";
   authError: boolean = false;

  constructor(private authenticationService: AuthenticationService, private authService: TokenService, private router: Router) { }

  ngOnInit(): void {
  }

  signIn() {
      this.authenticationService.authenticate({body: {email: this.email, password: this.password}}).subscribe(
          next => {
              this.authService.setToken(next.token);
              this.router.navigateByUrl("survey");
          }, error => this.authError = true
      )
  }

}
