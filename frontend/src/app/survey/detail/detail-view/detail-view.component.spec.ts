import {ComponentFixture, TestBed} from '@angular/core/testing';

import {DetailViewComponent} from './detail-view.component';
import {SurveysService} from "../../../api/services";
import {HttpClientTestingModule} from "@angular/common/http/testing";
import {TranslateModule} from "@ngx-translate/core";
import {RouterTestingModule} from "@angular/router/testing";
import {MatDialog} from "@angular/material/dialog";

describe('DetailViewComponent', () => {
    let component: DetailViewComponent;
    let fixture: ComponentFixture<DetailViewComponent>;

    beforeEach(async () => {
        await TestBed.configureTestingModule({
            declarations: [DetailViewComponent],
            imports: [HttpClientTestingModule, RouterTestingModule, TranslateModule.forRoot()],
            providers: [SurveysService, {provide: MatDialog, useValue: {}}]
        })
            .compileComponents();
    });

    beforeEach(() => {
        fixture = TestBed.createComponent(DetailViewComponent);
        component = fixture.componentInstance;
        fixture.detectChanges();
    });

    it('should create', () => {
        expect(component).toBeTruthy();
    });
});
