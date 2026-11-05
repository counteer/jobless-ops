import { TestBed } from '@angular/core/testing';
import { CompanyApi } from './company-api';

describe('CompanyApi', () => {
  let service: CompanyApi;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(CompanyApi);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
