import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { IUserGroup, UserGroup } from 'app/shared/model/user-group.model';
import { UserGroupService } from './user-group.service';
import { IUserPermissions } from 'app/shared/model/user-permissions.model';
import { UserPermissionsService } from 'app/entities/user-permissions/user-permissions.service';

@Component({
  selector: 'jhi-user-group-update',
  templateUrl: './user-group-update.component.html'
})
export class UserGroupUpdateComponent implements OnInit {
  isSaving = false;

  userpermissions: IUserPermissions[] = [];

  editForm = this.fb.group({
    id: [],
    groupName: [null, [Validators.required]],
    isActive: [],
    userPermissions: [null, Validators.required]
  });

  constructor(
    protected userGroupService: UserGroupService,
    protected userPermissionsService: UserPermissionsService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ userGroup }) => {
      this.updateForm(userGroup);

      this.userPermissionsService
        .query()
        .pipe(
          map((res: HttpResponse<IUserPermissions[]>) => {
            return res.body ? res.body : [];
          })
        )
        .subscribe((resBody: IUserPermissions[]) => (this.userpermissions = resBody));
    });
  }

  updateForm(userGroup: IUserGroup): void {
    this.editForm.patchValue({
      id: userGroup.id,
      groupName: userGroup.groupName,
      isActive: userGroup.isActive,
      userPermissions: userGroup.userPermissions
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const userGroup = this.createFromForm();
    if (userGroup.id !== undefined) {
      this.subscribeToSaveResponse(this.userGroupService.update(userGroup));
    } else {
      this.subscribeToSaveResponse(this.userGroupService.create(userGroup));
    }
  }

  private createFromForm(): IUserGroup {
    return {
      ...new UserGroup(),
      id: this.editForm.get(['id'])!.value,
      groupName: this.editForm.get(['groupName'])!.value,
      isActive: this.editForm.get(['isActive'])!.value,
      userPermissions: this.editForm.get(['userPermissions'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IUserGroup>>): void {
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

  trackById(index: number, item: IUserPermissions): any {
    return item.id;
  }

  getSelected(selectedVals: IUserPermissions[], option: IUserPermissions): IUserPermissions {
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
