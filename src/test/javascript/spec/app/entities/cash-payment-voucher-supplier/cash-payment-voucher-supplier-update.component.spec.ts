import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { WikunumTestModule } from '../../../test.module';
import { CashPaymentVoucherSupplierUpdateComponent } from 'app/entities/cash-payment-voucher-supplier/cash-payment-voucher-supplier-update.component';
import { CashPaymentVoucherSupplierService } from 'app/entities/cash-payment-voucher-supplier/cash-payment-voucher-supplier.service';
import { CashPaymentVoucherSupplier } from 'app/shared/model/cash-payment-voucher-supplier.model';

describe('Component Tests', () => {
  describe('CashPaymentVoucherSupplier Management Update Component', () => {
    let comp: CashPaymentVoucherSupplierUpdateComponent;
    let fixture: ComponentFixture<CashPaymentVoucherSupplierUpdateComponent>;
    let service: CashPaymentVoucherSupplierService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [WikunumTestModule],
        declarations: [CashPaymentVoucherSupplierUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(CashPaymentVoucherSupplierUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(CashPaymentVoucherSupplierUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(CashPaymentVoucherSupplierService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new CashPaymentVoucherSupplier(123);
        spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.update).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));

      it('Should call create service on save for new entity', fakeAsync(() => {
        // GIVEN
        const entity = new CashPaymentVoucherSupplier();
        spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));
    });
  });
});
