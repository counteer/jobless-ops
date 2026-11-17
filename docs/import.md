Import companies, job postings, and job applications with `POST /api/imports`:

```json
{
  "companies": [
    {
      "name": "Example Company",
      "website": "https://example.com",
      "jobPostings": [
        {
          "title": "Java Developer",
          "url": "https://example.com/jobs/java",
          "description": "Java and Spring",
          "jobApplications": [
            {
              "notes": "Applied through the company website",
              "appliedAt": "2026-11-17",
              "statusHistory": [
                { "statusCode": "APPLIED", "changedAt": "2026-11-17T09:00:00" },
                { "statusCode": "HR_SCREENING", "changedAt": "2026-11-18T14:30:00" }
              ]
            }
          ]
        }
      ]
    }
  ]
}
```

Each application belongs to its containing job posting. `appliedAt` is required
and uses `YYYY-MM-DD`; `notes` is optional. The `jobApplications` list may be
omitted, null, or empty, so existing company/posting import files still work.

The entire import runs in one transaction. Each request creates new records;
repeating an import does not deduplicate existing data.

Provide `statusHistory` to preserve dated changes. The current status is the entry
with the latest `changedAt`. Timestamps use local ISO date-time format.
Alternatively, provide `"statusCode": "APPLIED"` without `statusHistory` to create
one status entry at midnight on `appliedAt`. Both fields may be omitted for legacy
imports; supplying both is rejected. Unknown codes reject the entire import.
Existing inactive statuses can be imported to preserve historical records.

Available seeded codes: `APPLIED`, `HR_SCREENING`, `TECHNICAL_INTERVIEW`, `OFFER`,
`REJECTED`, `WITHDRAWN`.
