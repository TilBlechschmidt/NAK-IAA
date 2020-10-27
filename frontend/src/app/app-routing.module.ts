import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import {RegistrationDialogComponent} from "./authentication/registration-dialog/registration-dialog.component";
import {AuthenticationDialogComponent} from "./authentication/authentication-dialog/authentication-dialog.component";
import {SurveyTabViewComponent} from "./survey/survey-tab-view/survey-tab-view.component";
import {DetailViewComponent} from "./survey/detail/detail-view/detail-view.component";

const routes: Routes = [
    { path: "sign_up", component: RegistrationDialogComponent },
    { path: "sign_in", component: AuthenticationDialogComponent },
    { path: "survey", component: SurveyTabViewComponent },
    { path: "survey/detail/:id", component: DetailViewComponent },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
