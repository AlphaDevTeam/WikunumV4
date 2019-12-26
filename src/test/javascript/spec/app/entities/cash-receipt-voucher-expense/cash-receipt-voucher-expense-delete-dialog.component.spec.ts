import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { WikunumTestModule } from '../../../test.module';
import { MockEventManager } from '../../../helpers/mock-event-manager.service';
import { MockActiveModal } from '../../../helpers/mock-active-modal.service';
import { CashReceiptVoucherExpenseDeleteDialogComponent } from 'app/entities/cash-receipt-voucher-expense/cash-receipt-voucher-expense-delete-dialog.component';
import { CashReceiptVoucherExpenseService } from 'app/entities/cash-receipt-voucher-expense/cash-receipt-voucher-expense.service';

describe('Component Tests', () => {
  describe('CashReceiptVoucherExpense Management Delete Component', () => {
    let comp: CashReceiptVoucherExpenseDeleteDialogComponent;
    let fixture: ComponentFixture<CashReceiptVoucherExpenseDeleteDialogComponent>;
    let service: CashReceiptVoucherExpenseService;
    let mockEventManager: MockEventManager;
    let mockActiveModal: MockActiveModal;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [WikunumTestModule],
        declarations: [CashReceiptVoucherExpenseDeleteDialogComponent]
      })
        .overrideTemplate(CashReceiptVoucherExpenseDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(CashReceiptVoucherExpenseDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(CashReceiptVoucherExpenseService);
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
