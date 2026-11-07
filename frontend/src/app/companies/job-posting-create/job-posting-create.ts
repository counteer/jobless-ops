import { Component, inject, input, output, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { JobpostingApi } from '../jobposting-api';
import { JobPosting } from '../job-posting';
import { finalize } from 'rxjs';
import { CreateJobPostingRequest } from '../create-job-posting-request';

@Component({
  imports: [FormsModule],
  selector: 'app-job-posting-create',
  styleUrl: './job-posting-create.css',
  templateUrl: './job-posting-create.html',
})

export class JobPostingCreate {
  companyId = input.required<string>();
  title = '';
  url = '';
  description = ''
  private jobPostingApi = inject(JobpostingApi);
  jobPostingCreated = output<JobPosting>();
  errorMessage = signal<string | null>(null);
  loading = signal(false);

  
  createJobPosting(): void {
    if (this.loading()) {
      return;
    }
    if (!this.title.trim()) {
      this.errorMessage.set('Job posting name is required.');
      return;
    }

    this.errorMessage.set(null);

    const request: CreateJobPostingRequest = {
      companyId: this.companyId(),
      title: this.title.trim(),
      url: this.url.trim() || null,
      description: this.description || null
    };

    this.loading.set(true);


    this.jobPostingApi.createJobPosting(request).pipe(
      finalize(() => {
        this.loading.set(false);
      })).subscribe({
        next: (jobPosting) => {
          this.jobPostingCreated.emit(jobPosting);
        },
        error: (error) => {
          console.error('Failed to create jobPosting:', error);
        }
      });

  }




}
