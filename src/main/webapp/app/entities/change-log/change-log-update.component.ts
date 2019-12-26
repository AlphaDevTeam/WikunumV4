import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IChangeLog, ChangeLog } from 'app/shared/model/change-log.model';
import { ChangeLogService } from './change-log.service';

@Component({
  selector: 'jhi-change-log-update',
  templateUrl: './change-log-update.component.html'
})
export class ChangeLogUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    changeKey: [null, [Validators.required]],
    changeFrom: [null, [Validators.required]],
    changeTo: [null, [Validators.required]]
  });

  constructor(protected changeLogService: ChangeLogService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ changeLog }) => {
      this.updateForm(changeLog);
    });
  }

  updateForm(changeLog: IChangeLog): void {
    this.editForm.patchValue({
      id: changeLog.id,
      changeKey: changeLog.changeKey,
      changeFrom: changeLog.changeFrom,
      changeTo: changeLog.changeTo
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const changeLog = this.createFromForm();
    if (changeLog.id !== undefined) {
      this.subscribeToSaveResponse(this.changeLogService.update(changeLog));
    } else {
      this.subscribeToSaveResponse(this.changeLogService.create(changeLog));
    }
  }

  private createFromForm(): IChangeLog {
    return {
      ...new ChangeLog(),
      id: this.editForm.get(['id'])!.value,
      changeKey: this.editForm.get(['changeKey'])!.value,
      changeFrom: this.editForm.get(['changeFrom'])!.value,
      changeTo: this.editForm.get(['changeTo'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IChangeLog>>): void {
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
