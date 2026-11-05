import { Service } from '@angular/core';

import { inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Company } from './company';
import { Observable } from 'rxjs';
import { CreateCompanyRequest } from './create-company-request';

@Service()
export class CompanyApi {

  private http = inject(HttpClient);

  getCompanies(): Observable<Company[]> {
    return this.http.get<Company[]>('/api/companies');
  }

  createCompany(request: CreateCompanyRequest): Observable<Company> {
    return this.http.post<Company>('/api/companies', request);
  }
}
