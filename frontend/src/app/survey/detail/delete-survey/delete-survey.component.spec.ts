import {ComponentFixture, TestBed} from '@angular/core/testing';

import {DeleteSurveyComponent} from './delete-survey.component';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {HttpClientTestingModule} from "@angular/common/http/testing";
import {TranslateModule} from "@ngx-translate/core";
import {RouterTestingModule} from "@angular/router/testing";

describe('DeleteSurveyComponent', () => {
    let component: DeleteSurveyComponent;
    let fixture: ComponentFixture<DeleteSurveyComponent>;

    beforeEach(async () => {
        await TestBed.configureTestingModule({
            declarations: [DeleteSurveyComponent],
            imports: [RouterTestingModule, HttpClientTestingModule, TranslateModule.forRoot()],
            providers: [{provide: MatDialogRef, useValue: {}},
                {provide: MAT_DIALOG_DATA, useValue: {}}]
        })
            .compileComponents();
    });

    beforeEach(() => {
        fixture = TestBed.createComponent(DeleteSurveyComponent);
        component = fixture.componentInstance;
        fixture.detectChanges();
    });

    it('should create', () => {
        expect(component).toBeTruthy();
    });
});
