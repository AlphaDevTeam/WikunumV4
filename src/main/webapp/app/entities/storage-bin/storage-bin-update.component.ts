import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { IStorageBin, StorageBin } from 'app/shared/model/storage-bin.model';
import { StorageBinService } from './storage-bin.service';
import { IDocumentHistory } from 'app/shared/model/document-history.model';
import { DocumentHistoryService } from 'app/entities/document-history/document-history.service';

@Component({
  selector: 'jhi-storage-bin-update',
  templateUrl: './storage-bin-update.component.html'
})
export class StorageBinUpdateComponent implements OnInit {
  isSaving = false;

  histories: IDocumentHistory[] = [];

  editForm = this.fb.group({
    id: [],
    binNumber: [null, [Validators.required]],
    binDescription: [null, [Validators.required]],
    history: []
  });

  constructor(
    protected storageBinService: StorageBinService,
    protected documentHistoryService: DocumentHistoryService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ storageBin }) => {
      this.updateForm(storageBin);

      this.documentHistoryService
        .query({ 'storageBinId.specified': 'false' })
        .pipe(
          map((res: HttpResponse<IDocumentHistory[]>) => {
            return res.body ? res.body : [];
          })
        )
        .subscribe((resBody: IDocumentHistory[]) => {
          if (!storageBin.history || !storageBin.history.id) {
            this.histories = resBody;
          } else {
            this.documentHistoryService
              .find(storageBin.history.id)
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
    });
  }

  updateForm(storageBin: IStorageBin): void {
    this.editForm.patchValue({
      id: storageBin.id,
      binNumber: storageBin.binNumber,
      binDescription: storageBin.binDescription,
      history: storageBin.history
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const storageBin = this.createFromForm();
    if (storageBin.id !== undefined) {
      this.subscribeToSaveResponse(this.storageBinService.update(storageBin));
    } else {
      this.subscribeToSaveResponse(this.storageBinService.create(storageBin));
    }
  }

  private createFromForm(): IStorageBin {
    return {
      ...new StorageBin(),
      id: this.editForm.get(['id'])!.value,
      binNumber: this.editForm.get(['binNumber'])!.value,
      binDescription: this.editForm.get(['binDescription'])!.value,
      history: this.editForm.get(['history'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IStorageBin>>): void {
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

  trackById(index: number, item: IDocumentHistory): any {
    return item.id;
  }
}
