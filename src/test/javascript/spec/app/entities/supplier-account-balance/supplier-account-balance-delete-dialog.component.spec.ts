import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { WikunumTestModule } from '../../../test.module';
import { MockEventManager } from '../../../helpers/mock-event-manager.service';
import { MockActiveModal } from '../../../helpers/mock-active-modal.service';
import { SupplierAccountBalanceDeleteDialogComponent } from 'app/entities/supplier-account-balance/supplier-account-balance-delete-dialog.component';
import { SupplierAccountBalanceService } from 'app/entities/supplier-account-balance/supplier-account-balance.service';

describe('Component Tests', () => {
  describe('SupplierAccountBalance Management Delete Component', () => {
    let comp: SupplierAccountBalanceDeleteDialogComponent;
    let fixture: ComponentFixture<SupplierAccountBalanceDeleteDialogComponent>;
    let service: SupplierAccountBalanceService;
    let mockEventManager: MockEventManager;
    let mockActiveModal: MockActiveModal;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [WikunumTestModule],
        declarations: [SupplierAccountBalanceDeleteDialogComponent]
      })
        .overrideTemplate(SupplierAccountBalanceDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(SupplierAccountBalanceDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(SupplierAccountBalanceService);
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
