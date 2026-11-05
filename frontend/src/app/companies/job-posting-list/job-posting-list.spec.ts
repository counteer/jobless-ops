import { ComponentFixture, TestBed } from '@angular/core/testing';
import { JobPostingList } from './job-posting-list';

describe('JobPostingList', () => {
  let component: JobPostingList;
  let fixture: ComponentFixture<JobPostingList>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [JobPostingList],
    }).compileComponents();

    fixture = TestBed.createComponent(JobPostingList);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
