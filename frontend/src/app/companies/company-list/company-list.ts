import { Component, inject, OnInit, signal } from '@angular/core';
import { CompanyApi } from '../company-api';
import { Company } from '../company';
import { CompanyCreate } from '../company-create/company-create';
import { JobPostingList } from '../job-posting-list/job-posting-list';
import { catchError, finalize, of } from 'rxjs';
import { JobPostingCreate } from '../job-posting-create/job-posting-create';

@Component({
  imports: [JobPostingList, CompanyCreate, JobPostingCreate],
  selector: 'app-company-list',
  styleUrl: './company-list.css',
  templateUrl: './company-list.html',
})


export class CompanyList implements OnInit {
  private companyApi = inject(CompanyApi);
  companies = signal<Company[]>([]);
  selectedCompanyId = signal<string | null>(null);
  selectedCompanyName = signal<string | null>(null);
  showCreateForm = signal(false);
  jobPostingDialog: any;

  ngOnInit(): void {
    this.loadCompanies();
  }

  loadCompanies(): void {
    this.loading.set(true);
    this.errorMessage.set(null);
    this.companies.set([]);
    this.companyApi.getCompanies().pipe(
      catchError(error => {
        console.error('Failed to load companies:', error);
        this.errorMessage.set('Failed to load companies.');
        return of([] as Company[]);
      }),
      finalize(() => {
        this.loading.set(false);
      })
    ).subscribe(companies => {
      this.companies.set(companies);
    });
  }
 
  onCompanyCreated(company: Company): void {
    this.companies.update(companies => [...companies, company]);
  }

  loading = signal(false);
  errorMessage = signal<string | null>(null);
}