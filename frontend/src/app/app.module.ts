import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { AppbarComponent } from './appbar/appbar.component';
import { MatToolbarModule } from "@angular/material/toolbar";
import { AuthenticationButtonComponent } from './authentication/authentication-button/authentication-button.component';
import { RegistrationButtonComponent } from './authentication/registration-button/registration-button.component';
import { MatButtonModule } from "@angular/material/button";
import { AuthenticationDialogComponent } from './authentication/authentication-dialog/authentication-dialog.component';
import { MatCardModule } from "@angular/material/card";
import { MatFormFieldModule } from "@angular/material/form-field";
import { MatInputModule } from "@angular/material/input";
import { RegistrationDialogComponent } from './authentication/registration-dialog/registration-dialog.component';
import { SurveyTabViewComponent } from './survey/survey-tab-view/survey-tab-view.component';
import { MatTabsModule } from "@angular/material/tabs";
import { MatTableModule } from "@angular/material/table";
import { DetailViewComponent } from './survey/detail/detail-view/detail-view.component';
import { ResponseOverviewComponent } from './survey/detail/response-overview/response-overview.component';
import { MatIconModule } from "@angular/material/icon";
import { NewSurveyButtonComponent } from "./survey/create/new-survey-button/new-survey-button.component";
import { FixedAppointmentViewComponent } from "./survey/tabs/fixed-appointment-view/fixed-appointment-view.component";
import { DashboardComponent } from './survey/tabs/dashboard/dashboard.component';
import { NotificationComponent } from './survey/tabs/dashboard/notification/notification.component';
import { CreateSurveyDialogComponent } from './survey/create/create-survey-dialog/create-survey-dialog.component';
import {FormGroup, FormsModule, ReactiveFormsModule} from "@angular/forms";
import { MatDialogModule } from "@angular/material/dialog";
import { AuthenticationService } from "./api/services";
import {HttpClient, HttpClientModule} from "@angular/common/http";

@NgModule({
    declarations: [
        AppComponent,
        AppbarComponent,
        AuthenticationButtonComponent,
        RegistrationButtonComponent,
        AuthenticationDialogComponent,
        RegistrationDialogComponent,
        SurveyTabViewComponent,
        FixedAppointmentViewComponent,
        DetailViewComponent,
        ResponseOverviewComponent,
        NewSurveyButtonComponent,
        NewSurveyButtonComponent,
        FixedAppointmentViewComponent,
        DashboardComponent,
        NotificationComponent,
        CreateSurveyDialogComponent
    ],
    imports: [
        BrowserModule,
        AppRoutingModule,
        BrowserAnimationsModule,
        MatToolbarModule,
        MatButtonModule,
        MatCardModule,
        MatFormFieldModule,
        MatInputModule,
        MatTabsModule,
        MatTableModule,
        MatIconModule,
        MatDialogModule,
        FormsModule,
        HttpClientModule,
        ReactiveFormsModule
    ],
  providers: [
      AuthenticationService
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
