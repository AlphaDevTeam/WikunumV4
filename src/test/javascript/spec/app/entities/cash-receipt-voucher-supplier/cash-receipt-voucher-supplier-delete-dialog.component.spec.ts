import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { WikunumTestModule } from '../../../test.module';
import { MockEventManager } from '../../../helpers/mock-event-manager.service';
import { MockActiveModal } from '../../../helpers/mock-active-modal.service';
import { CashReceiptVoucherSupplierDeleteDialogComponent } from 'app/entities/cash-receipt-voucher-supplier/cash-receipt-voucher-supplier-delete-dialog.component';
import { CashReceiptVoucherSupplierService } from 'app/entities/cash-receipt-voucher-supplier/cash-receipt-voucher-supplier.service';

describe('Component Tests', () => {
  describe('CashReceiptVoucherSupplier Management Delete Component', () => {
    let comp: CashReceiptVoucherSupplierDeleteDialogComponent;
    let fixture: ComponentFixture<CashReceiptVoucherSupplierDeleteDialogComponent>;
    let service: CashReceiptVoucherSupplierService;
    let mockEventManager: MockEventManager;
    let mockActiveModal: MockActiveModal;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [WikunumTestModule],
        declarations: [CashReceiptVoucherSupplierDeleteDialogComponent]
      })
        .overrideTemplate(CashReceiptVoucherSupplierDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(CashReceiptVoucherSupplierDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(CashReceiptVoucherSupplierService);
      mockEventManager = TestBed.get(JhiEventManager);
      mockActiveModal = TestBed.get(NgbActiveModal);
    });

    describe('confirmDelete', () => {
      it('Should call delete service on confirmDelete', inject(
        [],
        fakeAsync(() => {
          // GIVEN
          spyOn(service, 'delete').and.returnValue(of({}));

          // WHEN
          comp.confirmDelete(123);
          tick();

          // THEN
          expect(service.delete).toHaveBeenCalledWith(123);
          expect(mockActiveModal.closeSpy).toHaveBeenCalled();
          expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
        })
      ));
      it('Should not call delete service on clear', () => {
        // GIVEN
        spyOn(service, 'delete');

        // WHEN
        comp.clear();

        // THEN
        expect(service.delete).not.toHaveBeenCalled();
        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
      });
    });
  });
});
