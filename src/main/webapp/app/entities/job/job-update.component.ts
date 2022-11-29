import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { IJob, Job } from 'app/shared/model/job.model';
import { JobService } from './job.service';
import { IJobStatus } from 'app/shared/model/job-status.model';
import { JobStatusService } from 'app/entities/job-status/job-status.service';
import { ILocation } from 'app/shared/model/location.model';
import { LocationService } from 'app/entities/location/location.service';
import { ICustomer } from 'app/shared/model/customer.model';
import { CustomerService } from 'app/entities/customer/customer.service';
import { IWorker } from 'app/shared/model/worker.model';
import { WorkerService } from 'app/entities/worker/worker.service';

type SelectableEntity = IJobStatus | ILocation | ICustomer | IWorker;

@Component({
  selector: 'jhi-job-update',
  templateUrl: './job-update.component.html'
})
export class JobUpdateComponent implements OnInit {
  isSaving = false;

  statuses: IJobStatus[] = [];

  locations: ILocation[] = [];

  customers: ICustomer[] = [];

  workers: IWorker[] = [];
  jobStartDateDp: any;
  jobEndDateDp: any;

  editForm = this.fb.group({
    id: [],
    jobCode: [null, [Validators.required]],
    jobDescription: [null, [Validators.required]],
    jobStartDate: [null, [Validators.required]],
    jobEndDate: [null, [Validators.required]],
    jobAmount: [null, [Validators.required]],
    status: [null, Validators.required],
    location: [null, Validators.required],
    customer: [null, Validators.required],
    assignedTos: [null, Validators.required]
  });

  constructor(
    protected jobService: JobService,
    protected jobStatusService: JobStatusService,
    protected locationService: LocationService,
    protected customerService: CustomerService,
    protected workerService: WorkerService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ job }) => {
      this.updateForm(job);

      this.jobStatusService
        .query({ 'jobId.specified': 'false' })
        .pipe(
          map((res: HttpResponse<IJobStatus[]>) => {
            return res.body ? res.body : [];
          })
        )
        .subscribe((resBody: IJobStatus[]) => {
          if (!job.status || !job.status.id) {
            this.statuses = resBody;
          } else {
            this.jobStatusService
              .find(job.status.id)
              .pipe(
                map((subRes: HttpResponse<IJobStatus>) => {
                  return subRes.body ? [subRes.body].concat(resBody) : resBody;
                })
              )
              .subscribe((concatRes: IJobStatus[]) => {
                this.statuses = concatRes;
              });
          }
        });

      this.locationService
        .query()
        .pipe(
          map((res: HttpResponse<ILocation[]>) => {
            return res.body ? res.body : [];
          })
        )
        .subscribe((resBody: ILocation[]) => (this.locations = resBody));

      this.customerService
        .query()
        .pipe(
          map((res: HttpResponse<ICustomer[]>) => {
            return res.body ? res.body : [];
          })
        )
        .subscribe((resBody: ICustomer[]) => (this.customers = resBody));

      this.workerService
        .query()
        .pipe(
          map((res: HttpResponse<IWorker[]>) => {
            return res.body ? res.body : [];
          })
        )
        .subscribe((resBody: IWorker[]) => (this.workers = resBody));
    });
  }

  updateForm(job: IJob): void {
    this.editForm.patchValue({
      id: job.id,
      jobCode: job.jobCode,
      jobDescription: job.jobDescription,
      jobStartDate: job.jobStartDate,
      jobEndDate: job.jobEndDate,
      jobAmount: job.jobAmount,
      status: job.status,
      location: job.location,
      customer: job.customer,
      assignedTos: job.assignedTos
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const job = this.createFromForm();
    if (job.id !== undefined) {
      this.subscribeToSaveResponse(this.jobService.update(job));
    } else {
      this.subscribeToSaveResponse(this.jobService.create(job));
    }
  }

  private createFromForm(): IJob {
    return {
      ...new Job(),
      id: this.editForm.get(['id'])!.value,
      jobCode: this.editForm.get(['jobCode'])!.value,
      jobDescription: this.editForm.get(['jobDescription'])!.value,
      jobStartDate: this.editForm.get(['jobStartDate'])!.value,
      jobEndDate: this.editForm.get(['jobEndDate'])!.value,
      jobAmount: this.editForm.get(['jobAmount'])!.value,
      status: this.editForm.get(['status'])!.value,
      location: this.editForm.get(['location'])!.value,
      customer: this.editForm.get(['customer'])!.value,
      assignedTos: this.editForm.get(['assignedTos'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IJob>>): void {
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

  trackById(index: number, item: SelectableEntity): any {
    return item.id;
  }

  getSelected(selectedVals: IWorker[], option: IWorker): IWorker {
    if (selectedVals) {
      for (let i = 0; i < selectedVals.length; i++) {
        if (option.id === selectedVals[i].id) {
          return selectedVals[i];
        }
      }
    }
    return option;
  }
}
