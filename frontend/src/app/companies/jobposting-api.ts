import { Service } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { inject } from '@angular/core';
import { Observable } from 'rxjs';
import { JobPosting } from './job-posting';
import { CreateJobPostingRequest } from './create-job-posting-request';

@Service()
export class JobpostingApi {

  private http = inject(HttpClient);

  getJobPostings(companyId: string): Observable<JobPosting[]> {
    return this.http.get<JobPosting[]>(`/api/companies/${companyId}/job-postings`);
  }

  createJobPosting(request: CreateJobPostingRequest): Observable<JobPosting> {
    return this.http.post<JobPosting>('/api/companies/${companyId}/job-postings', request);
  }

}
