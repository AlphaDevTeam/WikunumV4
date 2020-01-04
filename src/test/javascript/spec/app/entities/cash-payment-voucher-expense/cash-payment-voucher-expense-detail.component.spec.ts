import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { WikunumTestModule } from '../../../test.module';
import { CashPaymentVoucherExpenseDetailComponent } from 'app/entities/cash-payment-voucher-expense/cash-payment-voucher-expense-detail.component';
import { CashPaymentVoucherExpense } from 'app/shared/model/cash-payment-voucher-expense.model';

describe('Component Tests', () => {
  describe('CashPaymentVoucherExpense Management Detail Component', () => {
    let comp: CashPaymentVoucherExpenseDetailComponent;
    let fixture: ComponentFixture<CashPaymentVoucherExpenseDetailComponent>;
    const route = ({ data: of({ cashPaymentVoucherExpense: new CashPaymentVoucherExpense(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [WikunumTestModule],
        declarations: [CashPaymentVoucherExpenseDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(CashPaymentVoucherExpenseDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(CashPaymentVoucherExpenseDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load cashPaymentVoucherExpense on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.cashPaymentVoucherExpense).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
