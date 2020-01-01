import { Component, OnInit, ElementRef } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { JhiDataUtils, JhiFileLoadError, JhiEventManager, JhiEventWithContent } from 'ng-jhipster';

import { ILocation, Location } from 'app/shared/model/location.model';
import { LocationService } from './location.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { ICompany } from 'app/shared/model/company.model';
import { CompanyService } from 'app/entities/company/company.service';
import { IConfigurationItems } from 'app/shared/model/configuration-items.model';
import { ConfigurationItemsService } from 'app/entities/configuration-items/configuration-items.service';

type SelectableEntity = ICompany | IConfigurationItems;

@Component({
  selector: 'jhi-location-update',
  templateUrl: './location-update.component.html'
})
export class LocationUpdateComponent implements OnInit {
  isSaving = false;

  companies: ICompany[] = [];

  configurationitems: IConfigurationItems[] = [];

  editForm = this.fb.group({
    id: [],
    locationCode: [null, [Validators.required]],
    locationName: [null, [Validators.required]],
    isActive: [],
    email: [null, [Validators.required, Validators.pattern('^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$')]],
    rating: [],
    phone: [],
    addressLine1: [],
    addressLine2: [],
    city: [],
    country: [],
    image: [],
    imageContentType: [],
    vatNumber: [],
    vatPerc: [],
    company: [null, Validators.required],
    configitems: []
  });

  constructor(
    protected dataUtils: JhiDataUtils,
    protected eventManager: JhiEventManager,
    protected locationService: LocationService,
    protected companyService: CompanyService,
    protected configurationItemsService: ConfigurationItemsService,
    protected elementRef: ElementRef,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ location }) => {
      this.updateForm(location);

      this.companyService
        .query()
        .pipe(
          map((res: HttpResponse<ICompany[]>) => {
            return res.body ? res.body : [];
          })
        )
        .subscribe((resBody: ICompany[]) => (this.companies = resBody));

      this.configurationItemsService
        .query()
        .pipe(
          map((res: HttpResponse<IConfigurationItems[]>) => {
            return res.body ? res.body : [];
          })
        )
        .subscribe((resBody: IConfigurationItems[]) => (this.configurationitems = resBody));
    });
  }

  updateForm(location: ILocation): void {
    this.editForm.patchValue({
      id: location.id,
      locationCode: location.locationCode,
      locationName: location.locationName,
      isActive: location.isActive,
      email: location.email,
      rating: location.rating,
      phone: location.phone,
      addressLine1: location.addressLine1,
      addressLine2: location.addressLine2,
      city: location.city,
      country: location.country,
      image: location.image,
      imageContentType: location.imageContentType,
      vatNumber: location.vatNumber,
      vatPerc: location.vatPerc,
      company: location.company,
      configitems: location.configitems
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
    const location = this.createFromForm();
    if (location.id !== undefined) {
      this.subscribeToSaveResponse(this.locationService.update(location));
    } else {
      this.subscribeToSaveResponse(this.locationService.create(location));
    }
  }

  private createFromForm(): ILocation {
    return {
      ...new Location(),
      id: this.editForm.get(['id'])!.value,
      locationCode: this.editForm.get(['locationCode'])!.value,
      locationName: this.editForm.get(['locationName'])!.value,
      isActive: this.editForm.get(['isActive'])!.value,
      email: this.editForm.get(['email'])!.value,
      rating: this.editForm.get(['rating'])!.value,
      phone: this.editForm.get(['phone'])!.value,
      addressLine1: this.editForm.get(['addressLine1'])!.value,
      addressLine2: this.editForm.get(['addressLine2'])!.value,
      city: this.editForm.get(['city'])!.value,
      country: this.editForm.get(['country'])!.value,
      imageContentType: this.editForm.get(['imageContentType'])!.value,
      image: this.editForm.get(['image'])!.value,
      vatNumber: this.editForm.get(['vatNumber'])!.value,
      vatPerc: this.editForm.get(['vatPerc'])!.value,
      company: this.editForm.get(['company'])!.value,
      configitems: this.editForm.get(['configitems'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ILocation>>): void {
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

  getSelected(selectedVals: IConfigurationItems[], option: IConfigurationItems): IConfigurationItems {
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
