import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { WikunumTestModule } from '../../../test.module';
import { MockEventManager } from '../../../helpers/mock-event-manager.service';
import { MockActiveModal } from '../../../helpers/mock-active-modal.service';
import { CostOfSalesAccountDeleteDialogComponent } from 'app/entities/cost-of-sales-account/cost-of-sales-account-delete-dialog.component';
import { CostOfSalesAccountService } from 'app/entities/cost-of-sales-account/cost-of-sales-account.service';

describe('Component Tests', () => {
  describe('CostOfSalesAccount Management Delete Component', () => {
    let comp: CostOfSalesAccountDeleteDialogComponent;
    let fixture: ComponentFixture<CostOfSalesAccountDeleteDialogComponent>;
    let service: CostOfSalesAccountService;
    let mockEventManager: MockEventManager;
    let mockActiveModal: MockActiveModal;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [WikunumTestModule],
        declarations: [CostOfSalesAccountDeleteDialogComponent]
      })
        .overrideTemplate(CostOfSalesAccountDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(CostOfSalesAccountDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(CostOfSalesAccountService);
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
