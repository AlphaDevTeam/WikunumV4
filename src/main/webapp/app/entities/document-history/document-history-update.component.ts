import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';

import { IDocumentHistory, DocumentHistory } from 'app/shared/model/document-history.model';
import { DocumentHistoryService } from './document-history.service';
import { IDocumentType } from 'app/shared/model/document-type.model';
import { DocumentTypeService } from 'app/entities/document-type/document-type.service';
import { IExUser } from 'app/shared/model/ex-user.model';
import { ExUserService } from 'app/entities/ex-user/ex-user.service';
import { IChangeLog } from 'app/shared/model/change-log.model';
import { ChangeLogService } from 'app/entities/change-log/change-log.service';

type SelectableEntity = IDocumentType | IExUser | IChangeLog;

@Component({
  selector: 'jhi-document-history-update',
  templateUrl: './document-history-update.component.html'
})
export class DocumentHistoryUpdateComponent implements OnInit {
  isSaving = false;

  documenttypes: IDocumentType[] = [];

  exusers: IExUser[] = [];

  changelogs: IChangeLog[] = [];

  editForm = this.fb.group({
    id: [],
    historyDescription: [null, [Validators.required]],
    historyDate: [null, [Validators.required]],
    type: [null, Validators.required],
    lastModifiedUser: [null, Validators.required],
    createdUser: [null, Validators.required],
    changeLogs: []
  });

  constructor(
    protected documentHistoryService: DocumentHistoryService,
    protected documentTypeService: DocumentTypeService,
    protected exUserService: ExUserService,
    protected changeLogService: ChangeLogService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ documentHistory }) => {
      this.updateForm(documentHistory);

      this.documentTypeService
        .query()
        .pipe(
          map((res: HttpResponse<IDocumentType[]>) => {
            return res.body ? res.body : [];
          })
        )
        .subscribe((resBody: IDocumentType[]) => (this.documenttypes = resBody));

      this.exUserService
        .query()
        .pipe(
          map((res: HttpResponse<IExUser[]>) => {
            return res.body ? res.body : [];
          })
        )
        .subscribe((resBody: IExUser[]) => (this.exusers = resBody));

      this.changeLogService
        .query()
        .pipe(
          map((res: HttpResponse<IChangeLog[]>) => {
            return res.body ? res.body : [];
          })
        )
        .subscribe((resBody: IChangeLog[]) => (this.changelogs = resBody));
    });
  }

  updateForm(documentHistory: IDocumentHistory): void {
    this.editForm.patchValue({
      id: documentHistory.id,
      historyDescription: documentHistory.historyDescription,
      historyDate: documentHistory.historyDate != null ? documentHistory.historyDate.format(DATE_TIME_FORMAT) : null,
      type: documentHistory.type,
      lastModifiedUser: documentHistory.lastModifiedUser,
      createdUser: documentHistory.createdUser,
      changeLogs: documentHistory.changeLogs
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const documentHistory = this.createFromForm();
    if (documentHistory.id !== undefined) {
      this.subscribeToSaveResponse(this.documentHistoryService.update(documentHistory));
    } else {
      this.subscribeToSaveResponse(this.documentHistoryService.create(documentHistory));
    }
  }

  private createFromForm(): IDocumentHistory {
    return {
      ...new DocumentHistory(),
      id: this.editForm.get(['id'])!.value,
      historyDescription: this.editForm.get(['historyDescription'])!.value,
      historyDate:
        this.editForm.get(['historyDate'])!.value != null ? moment(this.editForm.get(['historyDate'])!.value, DATE_TIME_FORMAT) : undefined,
      type: this.editForm.get(['type'])!.value,
      lastModifiedUser: this.editForm.get(['lastModifiedUser'])!.value,
      createdUser: this.editForm.get(['createdUser'])!.value,
      changeLogs: this.editForm.get(['changeLogs'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDocumentHistory>>): void {
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

  getSelected(selectedVals: IChangeLog[], option: IChangeLog): IChangeLog {
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
