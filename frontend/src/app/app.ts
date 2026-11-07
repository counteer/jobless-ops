import { Component, signal } from '@angular/core';
import { CompanyList } from './companies/company-list/company-list';
import { CompanyCreate } from './companies/company-create/company-create';

@Component({
  imports: [CompanyList],
  selector: 'app-root',
  styleUrl: './app.css',
  templateUrl: './app.html',
})
export class App {}
