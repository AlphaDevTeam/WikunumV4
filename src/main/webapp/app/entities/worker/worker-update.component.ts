import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { IWorker, Worker } from 'app/shared/model/worker.model';
import { WorkerService } from './worker.service';
import { IDocumentHistory } from 'app/shared/model/document-history.model';
import { DocumentHistoryService } from 'app/entities/document-history/document-history.service';
import { ILocation } from 'app/shared/model/location.model';
import { LocationService } from 'app/entities/location/location.service';

type SelectableEntity = IDocumentHistory | ILocation;

@Component({
  selector: 'jhi-worker-update',
  templateUrl: './worker-update.component.html'
})
export class WorkerUpdateComponent implements OnInit {
  isSaving = false;

  histories: IDocumentHistory[] = [];

  locations: ILocation[] = [];

  editForm = this.fb.group({
    id: [],
    workerCode: [null, [Validators.required]],
    workerName: [null, [Validators.required]],
    workerLimit: [],
    isActive: [],
    rating: [],
    history: [],
    location: [null, Validators.required]
  });

  constructor(
    protected workerService: WorkerService,
    protected documentHistoryService: DocumentHistoryService,
    protected locationService: LocationService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ worker }) => {
      this.updateForm(worker);

      this.documentHistoryService
        .query({ 'workerId.specified': 'false' })
        .pipe(
          map((res: HttpResponse<IDocumentHistory[]>) => {
            return res.body ? res.body : [];
          })
        )
        .subscribe((resBody: IDocumentHistory[]) => {
          if (!worker.history || !worker.history.id) {
            this.histories = resBody;
          } else {
            this.documentHistoryService
              .find(worker.history.id)
              .pipe(
                map((subRes: HttpResponse<IDocumentHistory>) => {
                  return subRes.body ? [subRes.body].concat(resBody) : resBody;
                })
              )
              .subscribe((concatRes: IDocumentHistory[]) => {
                this.histories = concatRes;
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
    });
  }

  updateForm(worker: IWorker): void {
    this.editForm.patchValue({
      id: worker.id,
      workerCode: worker.workerCode,
      workerName: worker.workerName,
      workerLimit: worker.workerLimit,
      isActive: worker.isActive,
      rating: worker.rating,
      history: worker.history,
      location: worker.location
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const worker = this.createFromForm();
    if (worker.id !== undefined) {
      this.subscribeToSaveResponse(this.workerService.update(worker));
    } else {
      this.subscribeToSaveResponse(this.workerService.create(worker));
    }
  }

  private createFromForm(): IWorker {
    return {
      ...new Worker(),
      id: this.editForm.get(['id'])!.value,
      workerCode: this.editForm.get(['workerCode'])!.value,
      workerName: this.editForm.get(['workerName'])!.value,
      workerLimit: this.editForm.get(['workerLimit'])!.value,
      isActive: this.editForm.get(['isActive'])!.value,
      rating: this.editForm.get(['rating'])!.value,
      history: this.editForm.get(['history'])!.value,
      location: this.editForm.get(['location'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IWorker>>): void {
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
