import { Component, inject, input, signal } from '@angular/core';
import { JobpostingApi } from '../jobposting-api';
import { JobPosting } from '../job-posting';
import { takeUntilDestroyed, toObservable } from '@angular/core/rxjs-interop';
import { catchError, finalize, of, switchMap } from 'rxjs';

@Component({
  imports: [],
  selector: 'app-job-posting-list',
  styleUrl: './job-posting-list.css',
  templateUrl: './job-posting-list.html',
})

export class JobPostingList {

constructor() {
  toObservable(this.companyId)
    .pipe(
        switchMap(companyId => {
        this.loading.set(true);
        this.errorMessage.set(null);
        this.jobPostings.set([]);

      return this.jobpostingApi.getJobPostings(companyId).pipe(
        catchError(error => {
          console.error('Failed to load job postings:', error);
          this.errorMessage.set('Failed to load job postings.');
          return of([] as JobPosting[]);
        }),
        finalize(() => {
          this.loading.set(false);
        })
      );
    }),
    takeUntilDestroyed()
    )
    .subscribe({
      next: (jobPostings) => {
        this.jobPostings.set(jobPostings);
      },
      error: (error) => {
        console.error('Nem sikerült lekérni az álláshirdetéseket:', error);
      }
    });
  }
  companyId = input.required<string>();

  private jobpostingApi = inject(JobpostingApi);

  jobPostings = signal<JobPosting[]>([]);

  loading = signal(false);
  errorMessage = signal<string | null>(null);
}
