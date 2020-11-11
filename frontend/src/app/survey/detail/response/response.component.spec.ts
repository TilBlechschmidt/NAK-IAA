import {ComponentFixture, TestBed} from '@angular/core/testing';

import {ResponseComponent} from './response.component';
import {FormBuilder} from '@angular/forms';
import {TranslateModule, TranslateService} from '@ngx-translate/core';
import {APP_INITIALIZER} from '@angular/core';
import {initApp} from '../../../app.module';
import {HttpClient} from '@angular/common/http';
import {HttpClientTestingModule} from '@angular/common/http/testing';

describe('ResponseComponent', () => {
    let component: ResponseComponent;
    let fixture: ComponentFixture<ResponseComponent>;

    beforeEach(async () => {
        await TestBed.configureTestingModule({
            declarations: [ResponseComponent],
            imports: [TranslateModule.forRoot(), HttpClientTestingModule ],
            providers: [FormBuilder, {
                provide: APP_INITIALIZER,
                useFactory: initApp,
                deps: [HttpClient, TranslateService],
                multi: true
            }]
        }).compileComponents();
    });

    beforeEach(() => {
        fixture = TestBed.createComponent(ResponseComponent);
        component = fixture.componentInstance;
        fixture.detectChanges();
    });

    it('should create', () => {
        expect(component).toBeTruthy();
    });
});
