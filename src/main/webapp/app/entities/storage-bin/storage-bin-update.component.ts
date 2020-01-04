import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IStorageBin, StorageBin } from 'app/shared/model/storage-bin.model';
import { StorageBinService } from './storage-bin.service';

@Component({
  selector: 'jhi-storage-bin-update',
  templateUrl: './storage-bin-update.component.html'
})
export class StorageBinUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    binNumber: [null, [Validators.required]],
    binDescription: [null, [Validators.required]]
  });

  constructor(protected storageBinService: StorageBinService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ storageBin }) => {
      this.updateForm(storageBin);
    });
  }

  updateForm(storageBin: IStorageBin): void {
    this.editForm.patchValue({
      id: storageBin.id,
      binNumber: storageBin.binNumber,
      binDescription: storageBin.binDescription
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
      binDescription: this.editForm.get(['binDescription'])!.value
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
}
