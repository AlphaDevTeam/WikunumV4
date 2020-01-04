import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IConfigurationItems, ConfigurationItems } from 'app/shared/model/configuration-items.model';
import { ConfigurationItemsService } from './configuration-items.service';

@Component({
  selector: 'jhi-configuration-items-update',
  templateUrl: './configuration-items-update.component.html'
})
export class ConfigurationItemsUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    configCode: [null, [Validators.required]],
    configDescription: [null, [Validators.required]],
    configEnabled: [],
    configParamter: []
  });

  constructor(
    protected configurationItemsService: ConfigurationItemsService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ configurationItems }) => {
      this.updateForm(configurationItems);
    });
  }

  updateForm(configurationItems: IConfigurationItems): void {
    this.editForm.patchValue({
      id: configurationItems.id,
      configCode: configurationItems.configCode,
      configDescription: configurationItems.configDescription,
      configEnabled: configurationItems.configEnabled,
      configParamter: configurationItems.configParamter
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const configurationItems = this.createFromForm();
    if (configurationItems.id !== undefined) {
      this.subscribeToSaveResponse(this.configurationItemsService.update(configurationItems));
    } else {
      this.subscribeToSaveResponse(this.configurationItemsService.create(configurationItems));
    }
  }

  private createFromForm(): IConfigurationItems {
    return {
      ...new ConfigurationItems(),
      id: this.editForm.get(['id'])!.value,
      configCode: this.editForm.get(['configCode'])!.value,
      configDescription: this.editForm.get(['configDescription'])!.value,
      configEnabled: this.editForm.get(['configEnabled'])!.value,
      configParamter: this.editForm.get(['configParamter'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IConfigurationItems>>): void {
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
