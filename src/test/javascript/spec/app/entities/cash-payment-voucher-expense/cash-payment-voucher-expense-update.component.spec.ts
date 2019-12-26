import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { WikunumTestModule } from '../../../test.module';
import { CashPaymentVoucherExpenseUpdateComponent } from 'app/entities/cash-payment-voucher-expense/cash-payment-voucher-expense-update.component';
import { CashPaymentVoucherExpenseService } from 'app/entities/cash-payment-voucher-expense/cash-payment-voucher-expense.service';
import { CashPaymentVoucherExpense } from 'app/shared/model/cash-payment-voucher-expense.model';

describe('Component Tests', () => {
  describe('CashPaymentVoucherExpense Management Update Component', () => {
    let comp: CashPaymentVoucherExpenseUpdateComponent;
    let fixture: ComponentFixture<CashPaymentVoucherExpenseUpdateComponent>;
    let service: CashPaymentVoucherExpenseService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [WikunumTestModule],
        declarations: [CashPaymentVoucherExpenseUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(CashPaymentVoucherExpenseUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(CashPaymentVoucherExpenseUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(CashPaymentVoucherExpenseService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new CashPaymentVoucherExpense(123);
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
        const entity = new CashPaymentVoucherExpense();
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
