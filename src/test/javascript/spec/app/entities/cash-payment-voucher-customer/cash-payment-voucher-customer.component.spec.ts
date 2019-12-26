import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Data } from '@angular/router';

import { WikunumTestModule } from '../../../test.module';
import { CashPaymentVoucherCustomerComponent } from 'app/entities/cash-payment-voucher-customer/cash-payment-voucher-customer.component';
import { CashPaymentVoucherCustomerService } from 'app/entities/cash-payment-voucher-customer/cash-payment-voucher-customer.service';
import { CashPaymentVoucherCustomer } from 'app/shared/model/cash-payment-voucher-customer.model';

describe('Component Tests', () => {
  describe('CashPaymentVoucherCustomer Management Component', () => {
    let comp: CashPaymentVoucherCustomerComponent;
    let fixture: ComponentFixture<CashPaymentVoucherCustomerComponent>;
    let service: CashPaymentVoucherCustomerService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [WikunumTestModule],
        declarations: [CashPaymentVoucherCustomerComponent],
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
        .overrideTemplate(CashPaymentVoucherCustomerComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(CashPaymentVoucherCustomerComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(CashPaymentVoucherCustomerService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new CashPaymentVoucherCustomer(123)],
            headers
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.cashPaymentVoucherCustomers && comp.cashPaymentVoucherCustomers[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });

    it('should load a page', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new CashPaymentVoucherCustomer(123)],
            headers
          })
        )
      );

      // WHEN
      comp.loadPage(1);

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.cashPaymentVoucherCustomers && comp.cashPaymentVoucherCustomers[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });

    it('should re-initialize the page', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new CashPaymentVoucherCustomer(123)],
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
      expect(comp.cashPaymentVoucherCustomers && comp.cashPaymentVoucherCustomers[0]).toEqual(jasmine.objectContaining({ id: 123 }));
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