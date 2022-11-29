import { Component, OnInit, ElementRef } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { JhiDataUtils, JhiFileLoadError, JhiEventManager, JhiEventWithContent } from 'ng-jhipster';

import { IExUser, ExUser } from 'app/shared/model/ex-user.model';
import { ExUserService } from './ex-user.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { IUser } from 'app/core/user/user.model';
import { UserService } from 'app/core/user/user.service';
import { IDocumentHistory } from 'app/shared/model/document-history.model';
import { DocumentHistoryService } from 'app/entities/document-history/document-history.service';
import { ICompany } from 'app/shared/model/company.model';
import { CompanyService } from 'app/entities/company/company.service';
import { ILocation } from 'app/shared/model/location.model';
import { LocationService } from 'app/entities/location/location.service';
import { IUserGroup } from 'app/shared/model/user-group.model';
import { UserGroupService } from 'app/entities/user-group/user-group.service';
import { IUserPermissions } from 'app/shared/model/user-permissions.model';
import { UserPermissionsService } from 'app/entities/user-permissions/user-permissions.service';

type SelectableEntity = IUser | IDocumentHistory | ICompany | ILocation | IUserGroup | IUserPermissions;

type SelectableManyToManyEntity = ILocation | IUserGroup | IUserPermissions;

@Component({
  selector: 'jhi-ex-user-update',
  templateUrl: './ex-user-update.component.html'
})
export class ExUserUpdateComponent implements OnInit {
  isSaving = false;

  users: IUser[] = [];

  histories: IDocumentHistory[] = [];

  companies: ICompany[] = [];

  locations: ILocation[] = [];

  usergroups: IUserGroup[] = [];

  userpermissions: IUserPermissions[] = [];

  editForm = this.fb.group({
    id: [],
    userKey: [null, [Validators.required]],
    login: [null, [Validators.required]],
    firstName: [null, [Validators.required]],
    lastName: [],
    email: [null, [Validators.required, Validators.pattern('^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$')]],
    isActive: [],
    phone: [],
    addressLine1: [],
    addressLine2: [],
    city: [],
    country: [],
    image: [],
    imageContentType: [],
    userLimit: [],
    creditScore: [],
    relatedUser: [],
    history: [],
    company: [null, Validators.required],
    locations: [null, Validators.required],
    userGroups: [],
    userPermissions: []
  });

  constructor(
    protected dataUtils: JhiDataUtils,
    protected eventManager: JhiEventManager,
    protected exUserService: ExUserService,
    protected userService: UserService,
    protected documentHistoryService: DocumentHistoryService,
    protected companyService: CompanyService,
    protected locationService: LocationService,
    protected userGroupService: UserGroupService,
    protected userPermissionsService: UserPermissionsService,
    protected elementRef: ElementRef,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ exUser }) => {
      this.updateForm(exUser);

      this.userService
        .query()
        .pipe(
          map((res: HttpResponse<IUser[]>) => {
            return res.body ? res.body : [];
          })
        )
        .subscribe((resBody: IUser[]) => (this.users = resBody));

      this.documentHistoryService
        .query({ 'exUserId.specified': 'false' })
        .pipe(
          map((res: HttpResponse<IDocumentHistory[]>) => {
            return res.body ? res.body : [];
          })
        )
        .subscribe((resBody: IDocumentHistory[]) => {
          if (!exUser.history || !exUser.history.id) {
            this.histories = resBody;
          } else {
            this.documentHistoryService
              .find(exUser.history.id)
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

      this.locationService
        .query()
        .pipe(
          map((res: HttpResponse<ILocation[]>) => {
            return res.body ? res.body : [];
          })
        )
        .subscribe((resBody: ILocation[]) => (this.locations = resBody));

      this.userGroupService
        .query()
        .pipe(
          map((res: HttpResponse<IUserGroup[]>) => {
            return res.body ? res.body : [];
          })
        )
        .subscribe((resBody: IUserGroup[]) => (this.usergroups = resBody));

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

  updateForm(exUser: IExUser): void {
    this.editForm.patchValue({
      id: exUser.id,
      userKey: exUser.userKey,
      login: exUser.login,
      firstName: exUser.firstName,
      lastName: exUser.lastName,
      email: exUser.email,
      isActive: exUser.isActive,
      phone: exUser.phone,
      addressLine1: exUser.addressLine1,
      addressLine2: exUser.addressLine2,
      city: exUser.city,
      country: exUser.country,
      image: exUser.image,
      imageContentType: exUser.imageContentType,
      userLimit: exUser.userLimit,
      creditScore: exUser.creditScore,
      relatedUser: exUser.relatedUser,
      history: exUser.history,
      company: exUser.company,
      locations: exUser.locations,
      userGroups: exUser.userGroups,
      userPermissions: exUser.userPermissions
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
    const exUser = this.createFromForm();
    if (exUser.id !== undefined) {
      this.subscribeToSaveResponse(this.exUserService.update(exUser));
    } else {
      this.subscribeToSaveResponse(this.exUserService.create(exUser));
    }
  }

  private createFromForm(): IExUser {
    return {
      ...new ExUser(),
      id: this.editForm.get(['id'])!.value,
      userKey: this.editForm.get(['userKey'])!.value,
      login: this.editForm.get(['login'])!.value,
      firstName: this.editForm.get(['firstName'])!.value,
      lastName: this.editForm.get(['lastName'])!.value,
      email: this.editForm.get(['email'])!.value,
      isActive: this.editForm.get(['isActive'])!.value,
      phone: this.editForm.get(['phone'])!.value,
      addressLine1: this.editForm.get(['addressLine1'])!.value,
      addressLine2: this.editForm.get(['addressLine2'])!.value,
      city: this.editForm.get(['city'])!.value,
      country: this.editForm.get(['country'])!.value,
      imageContentType: this.editForm.get(['imageContentType'])!.value,
      image: this.editForm.get(['image'])!.value,
      userLimit: this.editForm.get(['userLimit'])!.value,
      creditScore: this.editForm.get(['creditScore'])!.value,
      relatedUser: this.editForm.get(['relatedUser'])!.value,
      history: this.editForm.get(['history'])!.value,
      company: this.editForm.get(['company'])!.value,
      locations: this.editForm.get(['locations'])!.value,
      userGroups: this.editForm.get(['userGroups'])!.value,
      userPermissions: this.editForm.get(['userPermissions'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IExUser>>): void {
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

  getSelected(selectedVals: SelectableManyToManyEntity[], option: SelectableManyToManyEntity): SelectableManyToManyEntity {
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
