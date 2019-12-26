import { Component, OnInit, ElementRef } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { JhiDataUtils, JhiFileLoadError, JhiEventManager, JhiEventWithContent } from 'ng-jhipster';

import { ISupplier, Supplier } from 'app/shared/model/supplier.model';
import { SupplierService } from './supplier.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { IDocumentHistory } from 'app/shared/model/document-history.model';
import { DocumentHistoryService } from 'app/entities/document-history/document-history.service';
import { ILocation } from 'app/shared/model/location.model';
import { LocationService } from 'app/entities/location/location.service';

type SelectableEntity = IDocumentHistory | ILocation;

@Component({
  selector: 'jhi-supplier-update',
  templateUrl: './supplier-update.component.html'
})
export class SupplierUpdateComponent implements OnInit {
  isSaving = false;

  histories: IDocumentHistory[] = [];

  locations: ILocation[] = [];

  editForm = this.fb.group({
    id: [],
    supplierCode: [null, [Validators.required]],
    supplierName: [null, [Validators.required]],
    supplierCreditLimit: [null, [Validators.required]],
    isActive: [],
    rating: [],
    phone: [],
    addressLine1: [],
    addressLine2: [],
    city: [],
    country: [],
    image: [],
    imageContentType: [],
    history: [],
    location: [null, Validators.required]
  });

  constructor(
    protected dataUtils: JhiDataUtils,
    protected eventManager: JhiEventManager,
    protected supplierService: SupplierService,
    protected documentHistoryService: DocumentHistoryService,
    protected locationService: LocationService,
    protected elementRef: ElementRef,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ supplier }) => {
      this.updateForm(supplier);

      this.documentHistoryService
        .query({ 'supplierId.specified': 'false' })
        .pipe(
          map((res: HttpResponse<IDocumentHistory[]>) => {
            return res.body ? res.body : [];
          })
        )
        .subscribe((resBody: IDocumentHistory[]) => {
          if (!supplier.history || !supplier.history.id) {
            this.histories = resBody;
          } else {
            this.documentHistoryService
              .find(supplier.history.id)
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

  updateForm(supplier: ISupplier): void {
    this.editForm.patchValue({
      id: supplier.id,
      supplierCode: supplier.supplierCode,
      supplierName: supplier.supplierName,
      supplierCreditLimit: supplier.supplierCreditLimit,
      isActive: supplier.isActive,
      rating: supplier.rating,
      phone: supplier.phone,
      addressLine1: supplier.addressLine1,
      addressLine2: supplier.addressLine2,
      city: supplier.city,
      country: supplier.country,
      image: supplier.image,
      imageContentType: supplier.imageContentType,
      history: supplier.history,
      location: supplier.location
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(contentType: string, base64String: string): void {
    this.dataUtils.openFile(contentType, base64String);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe(null, (err: JhiFileLoadError) => {
      this.eventManager.broadcast(
        new JhiEventWithContent<AlertError>('wikunumApp.error', { message: err.message })
      );
    });
  }

  clearInputImage(field: string, fieldContentType: string, idInput: string): void {
    this.editForm.patchValue({
      [field]: null,
      [fieldContentType]: null
    });
    if (this.elementRef && idInput && this.elementRef.nativeElement.querySelector('#' + idInput)) {
      this.elementRef.nativeElement.querySelector('#' + idInput).value = null;
    }
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const supplier = this.createFromForm();
    if (supplier.id !== undefined) {
      this.subscribeToSaveResponse(this.supplierService.update(supplier));
    } else {
      this.subscribeToSaveResponse(this.supplierService.create(supplier));
    }
  }

  private createFromForm(): ISupplier {
    return {
      ...new Supplier(),
      id: this.editForm.get(['id'])!.value,
      supplierCode: this.editForm.get(['supplierCode'])!.value,
      supplierName: this.editForm.get(['supplierName'])!.value,
      supplierCreditLimit: this.editForm.get(['supplierCreditLimit'])!.value,
      isActive: this.editForm.get(['isActive'])!.value,
      rating: this.editForm.get(['rating'])!.value,
      phone: this.editForm.get(['phone'])!.value,
      addressLine1: this.editForm.get(['addressLine1'])!.value,
      addressLine2: this.editForm.get(['addressLine2'])!.value,
      city: this.editForm.get(['city'])!.value,
      country: this.editForm.get(['country'])!.value,
      imageContentType: this.editForm.get(['imageContentType'])!.value,
      image: this.editForm.get(['image'])!.value,
      history: this.editForm.get(['history'])!.value,
      location: this.editForm.get(['location'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISupplier>>): void {
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
