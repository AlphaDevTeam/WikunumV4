import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { WikunumTestModule } from '../../../test.module';
import { CashReceiptVoucherExpenseUpdateComponent } from 'app/entities/cash-receipt-voucher-expense/cash-receipt-voucher-expense-update.component';
import { CashReceiptVoucherExpenseService } from 'app/entities/cash-receipt-voucher-expense/cash-receipt-voucher-expense.service';
import { CashReceiptVoucherExpense } from 'app/shared/model/cash-receipt-voucher-expense.model';

describe('Component Tests', () => {
  describe('CashReceiptVoucherExpense Management Update Component', () => {
    let comp: CashReceiptVoucherExpenseUpdateComponent;
    let fixture: ComponentFixture<CashReceiptVoucherExpenseUpdateComponent>;
    let service: CashReceiptVoucherExpenseService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [WikunumTestModule],
        declarations: [CashReceiptVoucherExpenseUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(CashReceiptVoucherExpenseUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(CashReceiptVoucherExpenseUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(CashReceiptVoucherExpenseService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new CashReceiptVoucherExpense(123);
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
        const entity = new CashReceiptVoucherExpense();
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
