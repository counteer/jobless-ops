import { Service } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { JobPosting } from './job-posting';

@Service()
export class JobpostingApi {

  private http = inject(HttpClient);

  getJobPostings(companyId: string): Observable<JobPosting[]> {
    return this.http.get<JobPosting[]>(`/api/companies/${companyId}/job-postings`);
  }

}
