import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { IJobDetails, JobDetails } from 'app/shared/model/job-details.model';
import { JobDetailsService } from './job-details.service';
import { IItems } from 'app/shared/model/items.model';
import { ItemsService } from 'app/entities/items/items.service';
import { IJob } from 'app/shared/model/job.model';
import { JobService } from 'app/entities/job/job.service';

type SelectableEntity = IItems | IJob;

@Component({
  selector: 'jhi-job-details-update',
  templateUrl: './job-details-update.component.html'
})
export class JobDetailsUpdateComponent implements OnInit {
  isSaving = false;

  items: IItems[] = [];

  jobs: IJob[] = [];

  editForm = this.fb.group({
    id: [],
    jobItemPrice: [null, [Validators.required]],
    jobItemQty: [null, [Validators.required]],
    item: [null, Validators.required],
    job: [null, Validators.required]
  });

  constructor(
    protected jobDetailsService: JobDetailsService,
    protected itemsService: ItemsService,
    protected jobService: JobService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ jobDetails }) => {
      this.updateForm(jobDetails);

      this.itemsService
        .query()
        .pipe(
          map((res: HttpResponse<IItems[]>) => {
            return res.body ? res.body : [];
          })
        )
        .subscribe((resBody: IItems[]) => (this.items = resBody));

      this.jobService
        .query()
        .pipe(
          map((res: HttpResponse<IJob[]>) => {
            return res.body ? res.body : [];
          })
        )
        .subscribe((resBody: IJob[]) => (this.jobs = resBody));
    });
  }

  updateForm(jobDetails: IJobDetails): void {
    this.editForm.patchValue({
      id: jobDetails.id,
      jobItemPrice: jobDetails.jobItemPrice,
      jobItemQty: jobDetails.jobItemQty,
      item: jobDetails.item,
      job: jobDetails.job
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const jobDetails = this.createFromForm();
    if (jobDetails.id !== undefined) {
      this.subscribeToSaveResponse(this.jobDetailsService.update(jobDetails));
    } else {
      this.subscribeToSaveResponse(this.jobDetailsService.create(jobDetails));
    }
  }

  private createFromForm(): IJobDetails {
    return {
      ...new JobDetails(),
      id: this.editForm.get(['id'])!.value,
      jobItemPrice: this.editForm.get(['jobItemPrice'])!.value,
      jobItemQty: this.editForm.get(['jobItemQty'])!.value,
      item: this.editForm.get(['item'])!.value,
      job: this.editForm.get(['job'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IJobDetails>>): void {
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
}
