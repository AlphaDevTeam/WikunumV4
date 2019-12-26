import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { ILocation, Location } from 'app/shared/model/location.model';
import { LocationService } from './location.service';
import { IDocumentHistory } from 'app/shared/model/document-history.model';
import { DocumentHistoryService } from 'app/entities/document-history/document-history.service';
import { ICompany } from 'app/shared/model/company.model';
import { CompanyService } from 'app/entities/company/company.service';
import { IConfigurationItems } from 'app/shared/model/configuration-items.model';
import { ConfigurationItemsService } from 'app/entities/configuration-items/configuration-items.service';

type SelectableEntity = IDocumentHistory | ICompany | IConfigurationItems;

@Component({
  selector: 'jhi-location-update',
  templateUrl: './location-update.component.html'
})
export class LocationUpdateComponent implements OnInit {
  isSaving = false;

  histories: IDocumentHistory[] = [];

  companies: ICompany[] = [];

  configurationitems: IConfigurationItems[] = [];

  editForm = this.fb.group({
    id: [],
    locationCode: [null, [Validators.required]],
    locationName: [null, [Validators.required]],
    isActive: [],
    history: [],
    company: [null, Validators.required],
    configitems: []
  });

  constructor(
    protected locationService: LocationService,
    protected documentHistoryService: DocumentHistoryService,
    protected companyService: CompanyService,
    protected configurationItemsService: ConfigurationItemsService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ location }) => {
      this.updateForm(location);

      this.documentHistoryService
        .query({ 'locationId.specified': 'false' })
        .pipe(
          map((res: HttpResponse<IDocumentHistory[]>) => {
            return res.body ? res.body : [];
          })
        )
        .subscribe((resBody: IDocumentHistory[]) => {
          if (!location.history || !location.history.id) {
            this.histories = resBody;
          } else {
            this.documentHistoryService
              .find(location.history.id)
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
      history: location.history,
      company: location.company,
      configitems: location.configitems
    });
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
      history: this.editForm.get(['history'])!.value,
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
