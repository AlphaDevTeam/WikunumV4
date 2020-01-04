import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Data } from '@angular/router';

import { WikunumTestModule } from '../../../test.module';
import { CashReceiptVoucherExpenseComponent } from 'app/entities/cash-receipt-voucher-expense/cash-receipt-voucher-expense.component';
import { CashReceiptVoucherExpenseService } from 'app/entities/cash-receipt-voucher-expense/cash-receipt-voucher-expense.service';
import { CashReceiptVoucherExpense } from 'app/shared/model/cash-receipt-voucher-expense.model';

describe('Component Tests', () => {
  describe('CashReceiptVoucherExpense Management Component', () => {
    let comp: CashReceiptVoucherExpenseComponent;
    let fixture: ComponentFixture<CashReceiptVoucherExpenseComponent>;
    let service: CashReceiptVoucherExpenseService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [WikunumTestModule],
        declarations: [CashReceiptVoucherExpenseComponent],
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
        .overrideTemplate(CashReceiptVoucherExpenseComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(CashReceiptVoucherExpenseComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(CashReceiptVoucherExpenseService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new CashReceiptVoucherExpense(123)],
            headers
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.cashReceiptVoucherExpenses && comp.cashReceiptVoucherExpenses[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });

    it('should load a page', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new CashReceiptVoucherExpense(123)],
            headers
          })
        )
      );

      // WHEN
      comp.loadPage(1);

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.cashReceiptVoucherExpenses && comp.cashReceiptVoucherExpenses[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });

    it('should calculate the sort attribute for an id', () => {
      // WHEN
      comp.ngOnInit();
      const result = comp.sort();

      // THEN
      expect(result).toEqual(['id,desc']);
    });

    it('should calculate the sort attribute for a non-id attribute', () => {
      // INIT
      comp.ngOnInit();

      // GIVEN
      comp.predicate = 'name';

      // WHEN
      const result = comp.sort();

      // THEN
      expect(result).toEqual(['name,desc', 'id']);
    });
  });
});
