import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { WikunumTestModule } from '../../../test.module';
import { CashReceiptVoucherSupplierDetailComponent } from 'app/entities/cash-receipt-voucher-supplier/cash-receipt-voucher-supplier-detail.component';
import { CashReceiptVoucherSupplier } from 'app/shared/model/cash-receipt-voucher-supplier.model';

describe('Component Tests', () => {
  describe('CashReceiptVoucherSupplier Management Detail Component', () => {
    let comp: CashReceiptVoucherSupplierDetailComponent;
    let fixture: ComponentFixture<CashReceiptVoucherSupplierDetailComponent>;
    const route = ({ data: of({ cashReceiptVoucherSupplier: new CashReceiptVoucherSupplier(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [WikunumTestModule],
        declarations: [CashReceiptVoucherSupplierDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(CashReceiptVoucherSupplierDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(CashReceiptVoucherSupplierDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load cashReceiptVoucherSupplier on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.cashReceiptVoucherSupplier).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
