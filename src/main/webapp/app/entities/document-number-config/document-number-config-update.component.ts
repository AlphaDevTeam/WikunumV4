import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { IDocumentNumberConfig, DocumentNumberConfig } from 'app/shared/model/document-number-config.model';
import { DocumentNumberConfigService } from './document-number-config.service';
import { IDocumentHistory } from 'app/shared/model/document-history.model';
import { DocumentHistoryService } from 'app/entities/document-history/document-history.service';
import { IDocumentType } from 'app/shared/model/document-type.model';
import { DocumentTypeService } from 'app/entities/document-type/document-type.service';
import { ILocation } from 'app/shared/model/location.model';
import { LocationService } from 'app/entities/location/location.service';
import { ITransactionType } from 'app/shared/model/transaction-type.model';
import { TransactionTypeService } from 'app/entities/transaction-type/transaction-type.service';

type SelectableEntity = IDocumentHistory | IDocumentType | ILocation | ITransactionType;

@Component({
  selector: 'jhi-document-number-config-update',
  templateUrl: './document-number-config-update.component.html'
})
export class DocumentNumberConfigUpdateComponent implements OnInit {
  isSaving = false;

  histories: IDocumentHistory[] = [];

  documenttypes: IDocumentType[] = [];

  locations: ILocation[] = [];

  transactiontypes: ITransactionType[] = [];

  editForm = this.fb.group({
    id: [],
    documentPrefix: [null, [Validators.required]],
    documentPostfix: [],
    currentNumber: [null, [Validators.required]],
    history: [],
    document: [null, Validators.required],
    location: [null, Validators.required],
    transactionType: [null, Validators.required]
  });

  constructor(
    protected documentNumberConfigService: DocumentNumberConfigService,
    protected documentHistoryService: DocumentHistoryService,
    protected documentTypeService: DocumentTypeService,
    protected locationService: LocationService,
    protected transactionTypeService: TransactionTypeService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ documentNumberConfig }) => {
      this.updateForm(documentNumberConfig);

      this.documentHistoryService
        .query({ 'documentNumberConfigId.specified': 'false' })
        .pipe(
          map((res: HttpResponse<IDocumentHistory[]>) => {
            return res.body ? res.body : [];
          })
        )
        .subscribe((resBody: IDocumentHistory[]) => {
          if (!documentNumberConfig.history || !documentNumberConfig.history.id) {
            this.histories = resBody;
          } else {
            this.documentHistoryService
              .find(documentNumberConfig.history.id)
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

      this.documentTypeService
        .query()
        .pipe(
          map((res: HttpResponse<IDocumentType[]>) => {
            return res.body ? res.body : [];
          })
        )
        .subscribe((resBody: IDocumentType[]) => (this.documenttypes = resBody));

      this.locationService
        .query()
        .pipe(
          map((res: HttpResponse<ILocation[]>) => {
            return res.body ? res.body : [];
          })
        )
        .subscribe((resBody: ILocation[]) => (this.locations = resBody));

      this.transactionTypeService
        .query()
        .pipe(
          map((res: HttpResponse<ITransactionType[]>) => {
            return res.body ? res.body : [];
          })
        )
        .subscribe((resBody: ITransactionType[]) => (this.transactiontypes = resBody));
    });
  }

  updateForm(documentNumberConfig: IDocumentNumberConfig): void {
    this.editForm.patchValue({
      id: documentNumberConfig.id,
      documentPrefix: documentNumberConfig.documentPrefix,
      documentPostfix: documentNumberConfig.documentPostfix,
      currentNumber: documentNumberConfig.currentNumber,
      history: documentNumberConfig.history,
      document: documentNumberConfig.document,
      location: documentNumberConfig.location,
      transactionType: documentNumberConfig.transactionType
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const documentNumberConfig = this.createFromForm();
    if (documentNumberConfig.id !== undefined) {
      this.subscribeToSaveResponse(this.documentNumberConfigService.update(documentNumberConfig));
    } else {
      this.subscribeToSaveResponse(this.documentNumberConfigService.create(documentNumberConfig));
    }
  }

  private createFromForm(): IDocumentNumberConfig {
    return {
      ...new DocumentNumberConfig(),
      id: this.editForm.get(['id'])!.value,
      documentPrefix: this.editForm.get(['documentPrefix'])!.value,
      documentPostfix: this.editForm.get(['documentPostfix'])!.value,
      currentNumber: this.editForm.get(['currentNumber'])!.value,
      history: this.editForm.get(['history'])!.value,
      document: this.editForm.get(['document'])!.value,
      location: this.editForm.get(['location'])!.value,
      transactionType: this.editForm.get(['transactionType'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDocumentNumberConfig>>): void {
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
