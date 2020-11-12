import { TestBed } from '@angular/core/testing';

import { ApiInterceptorService } from './api-interceptor.service';
import {RouterTestingModule} from '@angular/router/testing';

describe('ApiInterceptorService', () => {
  let service: ApiInterceptorService;

  beforeEach(() => {
    TestBed.configureTestingModule({
        imports: [RouterTestingModule]
    });
    service = TestBed.inject(ApiInterceptorService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
