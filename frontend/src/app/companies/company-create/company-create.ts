import { Component, inject } from '@angular/core';
import { CompanyApi } from '../company-api';
import { FormsModule } from '@angular/forms'
import { CreateCompanyRequest } from '../create-company-request';

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
  
  createCompany(): void {
  const request: CreateCompanyRequest = {
    name: this.name.trim(),
    website: this.website.trim() || null
  };

  this.companyApi.createCompany(request).subscribe({
    next: (company) => {
      console.log('Company created:', company);
    },
    error: (error) => {
      console.error('Failed to create company:', error);
    }
  });
}
}
