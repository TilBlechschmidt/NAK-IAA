import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { AppbarComponent } from './appbar/appbar.component';
import {MatToolbarModule} from "@angular/material/toolbar";
import { AuthenticationButtonComponent } from './authentication/authentication-button/authentication-button.component';
import { RegistrationButtonComponent } from './authentication/registration-button/registration-button.component';
import {MatButtonModule} from "@angular/material/button";
import { AuthenticationDialogComponent } from './authentication/authentication-dialog/authentication-dialog.component';
import {MatCardModule} from "@angular/material/card";
import {MatFormFieldModule} from "@angular/material/form-field";
import {MatInputModule} from "@angular/material/input";
import { RegistrationDialogComponent } from './authentication/registration-dialog/registration-dialog.component';
import { SurveyTabViewComponent } from './survey/survey-tab-view/survey-tab-view.component';

@NgModule({
  declarations: [
    AppComponent,
    AppbarComponent,
    AuthenticationButtonComponent,
    RegistrationButtonComponent,
    AuthenticationDialogComponent,
    RegistrationDialogComponent,
    SurveyTabViewComponent
  ],
    imports: [
        BrowserModule,
        AppRoutingModule,
        BrowserAnimationsModule,
        MatToolbarModule,
        MatButtonModule,
        MatCardModule,
        MatFormFieldModule,
        MatInputModule
    ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
