import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import {RegistrationDialogComponent} from "./authentication/registration-dialog/registration-dialog.component";
import {AuthenticationDialogComponent} from "./authentication/authentication-dialog/authentication-dialog.component";

const routes: Routes = [
    { path: "sign_up", component: RegistrationDialogComponent },
    { path: "sign_in", component: AuthenticationDialogComponent },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
