import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ISupplier } from 'app/shared/model/supplier.model';
import { SupplierService } from './supplier.service';

@Component({
  templateUrl: './supplier-delete-dialog.component.html'
})
export class SupplierDeleteDialogComponent {
  supplier?: ISupplier;

  constructor(protected supplierService: SupplierService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  clear(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.supplierService.delete(id).subscribe(() => {
      this.eventManager.broadcast('supplierListModification');
      this.activeModal.close();
    });
  }
}
