import {BrowserModule} from '@angular/platform-browser';
import {APP_INITIALIZER, forwardRef, NgModule, Provider} from '@angular/core';
import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {AppbarComponent} from './appbar/appbar.component';
import {MatToolbarModule} from '@angular/material/toolbar';
import {AuthenticationButtonComponent} from './authentication/authentication-button/authentication-button.component';
import {RegistrationButtonComponent} from './authentication/registration-button/registration-button.component';
import {MatButtonModule} from '@angular/material/button';
import {AuthenticationDialogComponent} from './authentication/authentication-dialog/authentication-dialog.component';
import {MatCardModule} from '@angular/material/card';
import {MatFormFieldModule} from '@angular/material/form-field';
import {MatInputModule} from '@angular/material/input';
import {RegistrationDialogComponent} from './authentication/registration-dialog/registration-dialog.component';
import {MatTabsModule} from '@angular/material/tabs';
import {MatTableModule} from '@angular/material/table';
import {ResponseOverviewComponent} from './survey/detail/response-overview/response-overview.component';
import {MatIconModule} from '@angular/material/icon';
import {NewSurveyButtonComponent} from './survey/create/new-survey-button/new-survey-button.component';
import {DashboardComponent} from './survey/tabs/dashboard/dashboard.component';
import {NotificationComponent} from './survey/tabs/dashboard/notification/notification.component';
import {CreateSurveyDialogComponent} from './survey/create/create-survey-dialog/create-survey-dialog.component';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {MatDialogModule} from '@angular/material/dialog';
import {HTTP_INTERCEPTORS, HttpClient, HttpClientModule} from '@angular/common/http';
import {TranslateModule, TranslateService} from '@ngx-translate/core';
import {forkJoin, of} from 'rxjs';
import {catchError} from 'rxjs/operators';
import {MatMenuModule} from '@angular/material/menu';
import {DeleteSurveyComponent} from './survey/detail/delete-survey/delete-survey.component';
import {LogoutButtonComponent} from './authentication/logout-button/logout-button.component';
import {LoggedInGuard} from './authentication/service/LoggedInGuard';
import {DetailViewComponent} from './survey/detail/detail-view/detail-view.component';
import {SurveyTabViewComponent} from './survey/survey-tab-view/survey-tab-view.component';
import {AbstractSurveyTableComponent} from './survey/tabs/abstract-survey-table/abstract-survey-table.component';
import {EditSurveyWarnComponent} from './survey/detail/edit-view-warn/edit-survey-warn.component';
import {ApiModule} from './api/api.module';
import {environment} from '../environments/environment';
import { ResponseComponent } from './survey/detail/response/response.component';
import {MatRippleModule} from '@angular/material/core';
import {ApiInterceptorService} from './authentication/service/api-interceptor.service';
import {PasswordConfirmationDialogComponent} from './authentication/password-confirmation-dialog/password-confirmation-dialog.component';
import { ResultOverviewComponent } from './survey/detail/result-overview/result-overview.component';

export const API_INTERCEPTOR_PROVIDER: Provider = {
    provide: HTTP_INTERCEPTORS,
    useExisting: forwardRef(() => ApiInterceptorService),
    multi: true
};


export function initApp(http: HttpClient, translate: TranslateService): () => Promise<boolean> {
    return () => new Promise<boolean>((resolve: (res: boolean) => void) => {

        const defaultLocale = 'en';
        const translationsUrl = '/assets/i18n/translations';
        const suffix = '.json';
        const storageLocale = localStorage.getItem('locale');
        const locale = storageLocale || defaultLocale;

        forkJoin([
            http.get(`/assets/i18n/dev.json`).pipe(
                catchError(() => of(null))
            ),
            http.get(`${translationsUrl}/${locale}${suffix}`).pipe(
                catchError(() => of(null))
            )
        ]).subscribe((response) => {
            const devKeys = response[0];
            const translatedKeys = response[1];

            translate.setTranslation(defaultLocale, devKeys || {});
            translate.setTranslation(locale, translatedKeys || {}, true);

            translate.setDefaultLang(defaultLocale);
            translate.use(locale);

            resolve(true);
        });
    });
}

const SelectedApiModule = environment.api.mocked ? ApiModule.mocked() : ApiModule.forRoot({rootUrl: environment.api.rootUrl});

@NgModule({
    declarations: [
        AppComponent,
        AppbarComponent,
        AuthenticationButtonComponent,
        RegistrationButtonComponent,
        AuthenticationDialogComponent,
        RegistrationDialogComponent,
        SurveyTabViewComponent,
        DetailViewComponent,
        ResponseOverviewComponent,
        NewSurveyButtonComponent,
        NewSurveyButtonComponent,
        DashboardComponent,
        NotificationComponent,
        CreateSurveyDialogComponent,
        DeleteSurveyComponent,
        LogoutButtonComponent,
        AbstractSurveyTableComponent,
        EditSurveyWarnComponent,
        ResponseComponent,
        PasswordConfirmationDialogComponent,
        ResultOverviewComponent
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
        ReactiveFormsModule,
        TranslateModule.forRoot(),
        SelectedApiModule,
        MatMenuModule,
        MatRippleModule
    ],
    providers: [
        LoggedInGuard,
        {
            provide: APP_INITIALIZER,
            useFactory: initApp,
            deps: [HttpClient, TranslateService],
            multi: true
        },
        ApiInterceptorService,
        API_INTERCEPTOR_PROVIDER
    ],
    bootstrap: [AppComponent]
})
export class AppModule {
}
