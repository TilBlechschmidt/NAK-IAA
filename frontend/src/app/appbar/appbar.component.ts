import { Component, OnInit } from '@angular/core';
import {TokenService} from '../authentication/service/token.service';
import {UserContextService} from '../authentication/service/user-context.service';

/**
 * @author Hendrik Reiter
 */

@Component({
  selector: 'app-appbar',
  templateUrl: './appbar.component.html',
  styleUrls: ['./appbar.component.sass']
})
export class AppbarComponent implements OnInit {

  constructor(private tokenService: TokenService, private userContextService: UserContextService) { }

  ngOnInit(): void {
  }

  changeLanguage(code: string): void {
      this.userContextService.setLanguage(code);
      window.location.reload();
  }

  isAuthenticated(): boolean {
      return this.tokenService.isAuthenticated();
  }

  getUserName(): string {
      return this.userContextService.getUserName();
  }

}
