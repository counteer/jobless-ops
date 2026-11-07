import { Component, inject, output, signal } from '@angular/core';
import { CompanyApi } from '../company-api';
import { FormsModule } from '@angular/forms'
import { CreateCompanyRequest } from '../create-company-request';
import { Company } from '../company';
import { finalize } from 'rxjs';

@Component({
  imports: [FormsModule],
  selector: 'app-company-create',
  styleUrl: './company-create.css',
  templateUrl: './company-create.html',
})
export class CompanyCreate {
  name = '';
  website = '';

  private companyApi = inject(CompanyApi);
  companyCreated = output<Company>();
  errorMessage = signal<string | null>(null);
  loading = signal(false);

  createCompany(): void {
    if (this.loading()) {
      return;
    }
    if (!this.name.trim()) {
      this.errorMessage.set('Company name is required.');
      return;
    }

    this.errorMessage.set(null);

    const request: CreateCompanyRequest = {
      name: this.name.trim(),
      website: this.website.trim() || null
    };

    this.companyApi.createCompany(request).pipe(
      finalize(() => {
        this.loading.set(false);
      })).subscribe({
        next: (company) => {
          this.loading.set(true);
          this.companyCreated.emit(company);
        },
        error: (error) => {
          console.error('Failed to create company:', error);
        }
      });
  }
}
