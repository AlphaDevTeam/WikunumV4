import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { IDocumentType, DocumentType } from 'app/shared/model/document-type.model';
import { DocumentTypeService } from './document-type.service';
import { IDocumentHistory } from 'app/shared/model/document-history.model';
import { DocumentHistoryService } from 'app/entities/document-history/document-history.service';

@Component({
  selector: 'jhi-document-type-update',
  templateUrl: './document-type-update.component.html'
})
export class DocumentTypeUpdateComponent implements OnInit {
  isSaving = false;

  histories: IDocumentHistory[] = [];

  editForm = this.fb.group({
    id: [],
    documentTypeCode: [null, [Validators.required]],
    documentType: [null, [Validators.required]],
    history: []
  });

  constructor(
    protected documentTypeService: DocumentTypeService,
    protected documentHistoryService: DocumentHistoryService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ documentType }) => {
      this.updateForm(documentType);

      this.documentHistoryService
        .query({ 'documentTypeId.specified': 'false' })
        .pipe(
          map((res: HttpResponse<IDocumentHistory[]>) => {
            return res.body ? res.body : [];
          })
        )
        .subscribe((resBody: IDocumentHistory[]) => {
          if (!documentType.history || !documentType.history.id) {
            this.histories = resBody;
          } else {
            this.documentHistoryService
              .find(documentType.history.id)
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

  updateForm(documentType: IDocumentType): void {
    this.editForm.patchValue({
      id: documentType.id,
      documentTypeCode: documentType.documentTypeCode,
      documentType: documentType.documentType,
      history: documentType.history
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const documentType = this.createFromForm();
    if (documentType.id !== undefined) {
      this.subscribeToSaveResponse(this.documentTypeService.update(documentType));
    } else {
      this.subscribeToSaveResponse(this.documentTypeService.create(documentType));
    }
  }

  private createFromForm(): IDocumentType {
    return {
      ...new DocumentType(),
      id: this.editForm.get(['id'])!.value,
      documentTypeCode: this.editForm.get(['documentTypeCode'])!.value,
      documentType: this.editForm.get(['documentType'])!.value,
      history: this.editForm.get(['history'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDocumentType>>): void {
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
