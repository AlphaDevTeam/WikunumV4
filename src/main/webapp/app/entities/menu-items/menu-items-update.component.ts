import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { IMenuItems, MenuItems } from 'app/shared/model/menu-items.model';
import { MenuItemsService } from './menu-items.service';
import { IDocumentHistory } from 'app/shared/model/document-history.model';
import { DocumentHistoryService } from 'app/entities/document-history/document-history.service';

@Component({
  selector: 'jhi-menu-items-update',
  templateUrl: './menu-items-update.component.html'
})
export class MenuItemsUpdateComponent implements OnInit {
  isSaving = false;

  histories: IDocumentHistory[] = [];

  editForm = this.fb.group({
    id: [],
    menuName: [null, [Validators.required]],
    menuURL: [null, [Validators.required]],
    isActive: [],
    history: []
  });

  constructor(
    protected menuItemsService: MenuItemsService,
    protected documentHistoryService: DocumentHistoryService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ menuItems }) => {
      this.updateForm(menuItems);

      this.documentHistoryService
        .query({ 'menuItemsId.specified': 'false' })
        .pipe(
          map((res: HttpResponse<IDocumentHistory[]>) => {
            return res.body ? res.body : [];
          })
        )
        .subscribe((resBody: IDocumentHistory[]) => {
          if (!menuItems.history || !menuItems.history.id) {
            this.histories = resBody;
          } else {
            this.documentHistoryService
              .find(menuItems.history.id)
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

  updateForm(menuItems: IMenuItems): void {
    this.editForm.patchValue({
      id: menuItems.id,
      menuName: menuItems.menuName,
      menuURL: menuItems.menuURL,
      isActive: menuItems.isActive,
      history: menuItems.history
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const menuItems = this.createFromForm();
    if (menuItems.id !== undefined) {
      this.subscribeToSaveResponse(this.menuItemsService.update(menuItems));
    } else {
      this.subscribeToSaveResponse(this.menuItemsService.create(menuItems));
    }
  }

  private createFromForm(): IMenuItems {
    return {
      ...new MenuItems(),
      id: this.editForm.get(['id'])!.value,
      menuName: this.editForm.get(['menuName'])!.value,
      menuURL: this.editForm.get(['menuURL'])!.value,
      isActive: this.editForm.get(['isActive'])!.value,
      history: this.editForm.get(['history'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMenuItems>>): void {
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
