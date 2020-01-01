import { Component, OnInit, ElementRef } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';
import { JhiDataUtils, JhiFileLoadError, JhiEventManager, JhiEventWithContent } from 'ng-jhipster';

import { ICompany, Company } from 'app/shared/model/company.model';
import { CompanyService } from './company.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { ILicenseType } from 'app/shared/model/license-type.model';
import { LicenseTypeService } from 'app/entities/license-type/license-type.service';

@Component({
  selector: 'jhi-company-update',
  templateUrl: './company-update.component.html'
})
export class CompanyUpdateComponent implements OnInit {
  isSaving = false;

  licensetypes: ILicenseType[] = [];
  expireOnDp: any;

  editForm = this.fb.group({
    id: [],
    companyCode: [null, [Validators.required]],
    companyName: [null, [Validators.required]],
    companyRegNumber: [null, [Validators.required]],
    email: [null, [Validators.required, Validators.pattern('^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$')]],
    rating: [],
    phone: [],
    addressLine1: [],
    addressLine2: [],
    city: [],
    country: [],
    image: [],
    imageContentType: [],
    isActive: [],
    apiKey: [],
    expireOn: [],
    licenseType: [null, Validators.required]
  });

  constructor(
    protected dataUtils: JhiDataUtils,
    protected eventManager: JhiEventManager,
    protected companyService: CompanyService,
    protected licenseTypeService: LicenseTypeService,
    protected elementRef: ElementRef,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ company }) => {
      this.updateForm(company);

      this.licenseTypeService
        .query()
        .pipe(
          map((res: HttpResponse<ILicenseType[]>) => {
            return res.body ? res.body : [];
          })
        )
        .subscribe((resBody: ILicenseType[]) => (this.licensetypes = resBody));
    });
  }

  updateForm(company: ICompany): void {
    this.editForm.patchValue({
      id: company.id,
      companyCode: company.companyCode,
      companyName: company.companyName,
      companyRegNumber: company.companyRegNumber,
      email: company.email,
      rating: company.rating,
      phone: company.phone,
      addressLine1: company.addressLine1,
      addressLine2: company.addressLine2,
      city: company.city,
      country: company.country,
      image: company.image,
      imageContentType: company.imageContentType,
      isActive: company.isActive,
      apiKey: company.apiKey,
      expireOn: company.expireOn,
      licenseType: company.licenseType
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
    const company = this.createFromForm();
    if (company.id !== undefined) {
      this.subscribeToSaveResponse(this.companyService.update(company));
    } else {
      this.subscribeToSaveResponse(this.companyService.create(company));
    }
  }

  private createFromForm(): ICompany {
    return {
      ...new Company(),
      id: this.editForm.get(['id'])!.value,
      companyCode: this.editForm.get(['companyCode'])!.value,
      companyName: this.editForm.get(['companyName'])!.value,
      companyRegNumber: this.editForm.get(['companyRegNumber'])!.value,
      email: this.editForm.get(['email'])!.value,
      rating: this.editForm.get(['rating'])!.value,
      phone: this.editForm.get(['phone'])!.value,
      addressLine1: this.editForm.get(['addressLine1'])!.value,
      addressLine2: this.editForm.get(['addressLine2'])!.value,
      city: this.editForm.get(['city'])!.value,
      country: this.editForm.get(['country'])!.value,
      imageContentType: this.editForm.get(['imageContentType'])!.value,
      image: this.editForm.get(['image'])!.value,
      isActive: this.editForm.get(['isActive'])!.value,
      apiKey: this.editForm.get(['apiKey'])!.value,
      expireOn: this.editForm.get(['expireOn'])!.value,
      licenseType: this.editForm.get(['licenseType'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICompany>>): void {
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

  trackById(index: number, item: ILicenseType): any {
    return item.id;
  }
}
