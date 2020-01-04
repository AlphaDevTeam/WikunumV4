import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IJobStatus } from 'app/shared/model/job-status.model';

@Component({
  selector: 'jhi-job-status-detail',
  templateUrl: './job-status-detail.component.html'
})
export class JobStatusDetailComponent implements OnInit {
  jobStatus: IJobStatus | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ jobStatus }) => {
      this.jobStatus = jobStatus;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
