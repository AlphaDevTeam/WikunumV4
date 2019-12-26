import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { WikunumTestModule } from '../../../test.module';
import { CashReceiptVoucherCustomerUpdateComponent } from 'app/entities/cash-receipt-voucher-customer/cash-receipt-voucher-customer-update.component';
import { CashReceiptVoucherCustomerService } from 'app/entities/cash-receipt-voucher-customer/cash-receipt-voucher-customer.service';
import { CashReceiptVoucherCustomer } from 'app/shared/model/cash-receipt-voucher-customer.model';

describe('Component Tests', () => {
  describe('CashReceiptVoucherCustomer Management Update Component', () => {
    let comp: CashReceiptVoucherCustomerUpdateComponent;
    let fixture: ComponentFixture<CashReceiptVoucherCustomerUpdateComponent>;
    let service: CashReceiptVoucherCustomerService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [WikunumTestModule],
        declarations: [CashReceiptVoucherCustomerUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(CashReceiptVoucherCustomerUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(CashReceiptVoucherCustomerUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(CashReceiptVoucherCustomerService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new CashReceiptVoucherCustomer(123);
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
        const entity = new CashReceiptVoucherCustomer();
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
