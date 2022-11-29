import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IEmployeeType } from 'app/shared/model/employee-type.model';
import { EmployeeTypeService } from './employee-type.service';

@Component({
  templateUrl: './employee-type-delete-dialog.component.html'
})
export class EmployeeTypeDeleteDialogComponent {
  employeeType?: IEmployeeType;

  constructor(
    protected employeeTypeService: EmployeeTypeService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  clear(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.employeeTypeService.delete(id).subscribe(() => {
      this.eventManager.broadcast('employeeTypeListModification');
      this.activeModal.close();
    });
  }
}
