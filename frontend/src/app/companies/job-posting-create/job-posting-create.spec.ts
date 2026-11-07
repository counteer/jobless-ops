import { ComponentFixture, TestBed } from '@angular/core/testing';
import { JobPostingCreate } from './job-posting-create';

describe('JobPostingCreate', () => {
  let component: JobPostingCreate;
  let fixture: ComponentFixture<JobPostingCreate>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [JobPostingCreate],
    }).compileComponents();

    fixture = TestBed.createComponent(JobPostingCreate);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
