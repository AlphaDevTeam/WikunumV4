import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { IJobStatus, JobStatus } from 'app/shared/model/job-status.model';
import { JobStatusService } from './job-status.service';
import { ILocation } from 'app/shared/model/location.model';
import { LocationService } from 'app/entities/location/location.service';

@Component({
  selector: 'jhi-job-status-update',
  templateUrl: './job-status-update.component.html'
})
export class JobStatusUpdateComponent implements OnInit {
  isSaving = false;

  locations: ILocation[] = [];

  editForm = this.fb.group({
    id: [],
    jobStatusCode: [null, [Validators.required]],
    jobStatusDescription: [null, [Validators.required]],
    isActive: [null, [Validators.required]],
    location: [null, Validators.required]
  });

  constructor(
    protected jobStatusService: JobStatusService,
    protected locationService: LocationService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ jobStatus }) => {
      this.updateForm(jobStatus);

      this.locationService
        .query()
        .pipe(
          map((res: HttpResponse<ILocation[]>) => {
            return res.body ? res.body : [];
          })
        )
        .subscribe((resBody: ILocation[]) => (this.locations = resBody));
    });
  }

  updateForm(jobStatus: IJobStatus): void {
    this.editForm.patchValue({
      id: jobStatus.id,
      jobStatusCode: jobStatus.jobStatusCode,
      jobStatusDescription: jobStatus.jobStatusDescription,
      isActive: jobStatus.isActive,
      location: jobStatus.location
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const jobStatus = this.createFromForm();
    if (jobStatus.id !== undefined) {
      this.subscribeToSaveResponse(this.jobStatusService.update(jobStatus));
    } else {
      this.subscribeToSaveResponse(this.jobStatusService.create(jobStatus));
    }
  }

  private createFromForm(): IJobStatus {
    return {
      ...new JobStatus(),
      id: this.editForm.get(['id'])!.value,
      jobStatusCode: this.editForm.get(['jobStatusCode'])!.value,
      jobStatusDescription: this.editForm.get(['jobStatusDescription'])!.value,
      isActive: this.editForm.get(['isActive'])!.value,
      location: this.editForm.get(['location'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IJobStatus>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }

  trackById(index: number, item: ILocation): any {
    return item.id;
  }
}
