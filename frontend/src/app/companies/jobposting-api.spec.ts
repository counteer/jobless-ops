import { TestBed } from '@angular/core/testing';
import { JobpostingApi } from './jobposting-api';

describe('JobpostingApi', () => {
  let service: JobpostingApi;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(JobpostingApi);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
