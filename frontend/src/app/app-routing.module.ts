import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import {RegistrationDialogComponent} from './authentication/registration-dialog/registration-dialog.component';
import {AuthenticationDialogComponent} from './authentication/authentication-dialog/authentication-dialog.component';
import {SurveyTabViewComponent} from './survey/survey-tab-view/survey-tab-view.component';
import {DetailViewComponent} from './survey/detail/detail-view/detail-view.component';
import {LoggedInGuard} from './authentication/service/LoggedInGuard';

const routes: Routes = [
    { path: 'sign_up', component: RegistrationDialogComponent },
    { path: 'sign_in', component: AuthenticationDialogComponent },
    { path: 'survey', component: SurveyTabViewComponent, canActivate: [LoggedInGuard] },
    { path: 'survey/detail/:id', component: DetailViewComponent, canActivate: [LoggedInGuard] },
];


@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
