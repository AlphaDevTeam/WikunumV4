import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { WikunumTestModule } from '../../../test.module';
import { CashReceiptVoucherCustomerDetailComponent } from 'app/entities/cash-receipt-voucher-customer/cash-receipt-voucher-customer-detail.component';
import { CashReceiptVoucherCustomer } from 'app/shared/model/cash-receipt-voucher-customer.model';

describe('Component Tests', () => {
  describe('CashReceiptVoucherCustomer Management Detail Component', () => {
    let comp: CashReceiptVoucherCustomerDetailComponent;
    let fixture: ComponentFixture<CashReceiptVoucherCustomerDetailComponent>;
    const route = ({ data: of({ cashReceiptVoucherCustomer: new CashReceiptVoucherCustomer(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [WikunumTestModule],
        declarations: [CashReceiptVoucherCustomerDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(CashReceiptVoucherCustomerDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(CashReceiptVoucherCustomerDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load cashReceiptVoucherCustomer on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.cashReceiptVoucherCustomer).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
