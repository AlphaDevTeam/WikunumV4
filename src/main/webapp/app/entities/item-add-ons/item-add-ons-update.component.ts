import { Component, OnInit, ElementRef } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { JhiDataUtils, JhiFileLoadError, JhiEventManager, JhiEventWithContent } from 'ng-jhipster';

import { IItemAddOns, ItemAddOns } from 'app/shared/model/item-add-ons.model';
import { ItemAddOnsService } from './item-add-ons.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { ILocation } from 'app/shared/model/location.model';
import { LocationService } from 'app/entities/location/location.service';

@Component({
  selector: 'jhi-item-add-ons-update',
  templateUrl: './item-add-ons-update.component.html'
})
export class ItemAddOnsUpdateComponent implements OnInit {
  isSaving = false;

  locations: ILocation[] = [];

  editForm = this.fb.group({
    id: [],
    addonCode: [null, [Validators.required]],
    addonName: [null, [Validators.required]],
    addonDescription: [],
    isActive: [],
    allowSubstract: [],
    addonPrice: [],
    substractPrice: [],
    image: [],
    imageContentType: [],
    location: [null, Validators.required]
  });

  constructor(
    protected dataUtils: JhiDataUtils,
    protected eventManager: JhiEventManager,
    protected itemAddOnsService: ItemAddOnsService,
    protected locationService: LocationService,
    protected elementRef: ElementRef,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ itemAddOns }) => {
      this.updateForm(itemAddOns);

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

  updateForm(itemAddOns: IItemAddOns): void {
    this.editForm.patchValue({
      id: itemAddOns.id,
      addonCode: itemAddOns.addonCode,
      addonName: itemAddOns.addonName,
      addonDescription: itemAddOns.addonDescription,
      isActive: itemAddOns.isActive,
      allowSubstract: itemAddOns.allowSubstract,
      addonPrice: itemAddOns.addonPrice,
      substractPrice: itemAddOns.substractPrice,
      image: itemAddOns.image,
      imageContentType: itemAddOns.imageContentType,
      location: itemAddOns.location
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
    const itemAddOns = this.createFromForm();
    if (itemAddOns.id !== undefined) {
      this.subscribeToSaveResponse(this.itemAddOnsService.update(itemAddOns));
    } else {
      this.subscribeToSaveResponse(this.itemAddOnsService.create(itemAddOns));
    }
  }

  private createFromForm(): IItemAddOns {
    return {
      ...new ItemAddOns(),
      id: this.editForm.get(['id'])!.value,
      addonCode: this.editForm.get(['addonCode'])!.value,
      addonName: this.editForm.get(['addonName'])!.value,
      addonDescription: this.editForm.get(['addonDescription'])!.value,
      isActive: this.editForm.get(['isActive'])!.value,
      allowSubstract: this.editForm.get(['allowSubstract'])!.value,
      addonPrice: this.editForm.get(['addonPrice'])!.value,
      substractPrice: this.editForm.get(['substractPrice'])!.value,
      imageContentType: this.editForm.get(['imageContentType'])!.value,
      image: this.editForm.get(['image'])!.value,
      location: this.editForm.get(['location'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IItemAddOns>>): void {
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

  trackById(index: number, item: ILocation): any {
    return item.id;
  }
}
