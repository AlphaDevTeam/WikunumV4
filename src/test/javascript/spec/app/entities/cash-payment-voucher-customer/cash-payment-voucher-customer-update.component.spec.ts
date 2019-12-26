import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { WikunumTestModule } from '../../../test.module';
import { CashPaymentVoucherCustomerUpdateComponent } from 'app/entities/cash-payment-voucher-customer/cash-payment-voucher-customer-update.component';
import { CashPaymentVoucherCustomerService } from 'app/entities/cash-payment-voucher-customer/cash-payment-voucher-customer.service';
import { CashPaymentVoucherCustomer } from 'app/shared/model/cash-payment-voucher-customer.model';

describe('Component Tests', () => {
  describe('CashPaymentVoucherCustomer Management Update Component', () => {
    let comp: CashPaymentVoucherCustomerUpdateComponent;
    let fixture: ComponentFixture<CashPaymentVoucherCustomerUpdateComponent>;
    let service: CashPaymentVoucherCustomerService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [WikunumTestModule],
        declarations: [CashPaymentVoucherCustomerUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(CashPaymentVoucherCustomerUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(CashPaymentVoucherCustomerUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(CashPaymentVoucherCustomerService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new CashPaymentVoucherCustomer(123);
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
        const entity = new CashPaymentVoucherCustomer();
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
