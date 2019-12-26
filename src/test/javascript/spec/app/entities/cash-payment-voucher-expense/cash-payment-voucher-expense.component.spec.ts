import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Data } from '@angular/router';

import { WikunumTestModule } from '../../../test.module';
import { CashPaymentVoucherExpenseComponent } from 'app/entities/cash-payment-voucher-expense/cash-payment-voucher-expense.component';
import { CashPaymentVoucherExpenseService } from 'app/entities/cash-payment-voucher-expense/cash-payment-voucher-expense.service';
import { CashPaymentVoucherExpense } from 'app/shared/model/cash-payment-voucher-expense.model';

describe('Component Tests', () => {
  describe('CashPaymentVoucherExpense Management Component', () => {
    let comp: CashPaymentVoucherExpenseComponent;
    let fixture: ComponentFixture<CashPaymentVoucherExpenseComponent>;
    let service: CashPaymentVoucherExpenseService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [WikunumTestModule],
        declarations: [CashPaymentVoucherExpenseComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: {
              data: {
                subscribe: (fn: (value: Data) => void) =>
                  fn({
                    pagingParams: {
                      predicate: 'id',
                      reverse: false,
                      page: 0
                    }
                  })
              }
            }
          }
        ]
      })
        .overrideTemplate(CashPaymentVoucherExpenseComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(CashPaymentVoucherExpenseComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(CashPaymentVoucherExpenseService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new CashPaymentVoucherExpense(123)],
            headers
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.cashPaymentVoucherExpenses && comp.cashPaymentVoucherExpenses[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });

    it('should load a page', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new CashPaymentVoucherExpense(123)],
            headers
          })
        )
      );

      // WHEN
      comp.loadPage(1);

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.cashPaymentVoucherExpenses && comp.cashPaymentVoucherExpenses[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });

    it('should re-initialize the page', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new CashPaymentVoucherExpense(123)],
            headers
          })
        )
      );

      // WHEN
      comp.loadPage(1);
      comp.reset();

      // THEN
      expect(comp.page).toEqual(0);
      expect(service.query).toHaveBeenCalledTimes(2);
      expect(comp.cashPaymentVoucherExpenses && comp.cashPaymentVoucherExpenses[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });

    it('should calculate the sort attribute for an id', () => {
      // WHEN
      comp.ngOnInit();
      const result = comp.sort();

      // THEN
      expect(result).toEqual(['id,asc']);
    });

    it('should calculate the sort attribute for a non-id attribute', () => {
      // INIT
      comp.ngOnInit();

      // GIVEN
      comp.predicate = 'name';

      // WHEN
      const result = comp.sort();

      // THEN
      expect(result).toEqual(['name,asc', 'id']);
    });
  });
});
