import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { WikunumTestModule } from '../../../test.module';
import { MockEventManager } from '../../../helpers/mock-event-manager.service';
import { MockActiveModal } from '../../../helpers/mock-active-modal.service';
import { PaymentTypeBalanceDeleteDialogComponent } from 'app/entities/payment-type-balance/payment-type-balance-delete-dialog.component';
import { PaymentTypeBalanceService } from 'app/entities/payment-type-balance/payment-type-balance.service';

describe('Component Tests', () => {
  describe('PaymentTypeBalance Management Delete Component', () => {
    let comp: PaymentTypeBalanceDeleteDialogComponent;
    let fixture: ComponentFixture<PaymentTypeBalanceDeleteDialogComponent>;
    let service: PaymentTypeBalanceService;
    let mockEventManager: MockEventManager;
    let mockActiveModal: MockActiveModal;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [WikunumTestModule],
        declarations: [PaymentTypeBalanceDeleteDialogComponent]
      })
        .overrideTemplate(PaymentTypeBalanceDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(PaymentTypeBalanceDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(PaymentTypeBalanceService);
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
