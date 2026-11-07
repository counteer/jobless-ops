export interface CreateJobPostingRequest {
  companyId: string;
  title: string;
  url: string | null;
  description: string | null;
}