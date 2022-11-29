import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { WikunumTestModule } from '../../../test.module';
import { CashReceiptVoucherSupplierUpdateComponent } from 'app/entities/cash-receipt-voucher-supplier/cash-receipt-voucher-supplier-update.component';
import { CashReceiptVoucherSupplierService } from 'app/entities/cash-receipt-voucher-supplier/cash-receipt-voucher-supplier.service';
import { CashReceiptVoucherSupplier } from 'app/shared/model/cash-receipt-voucher-supplier.model';

describe('Component Tests', () => {
  describe('CashReceiptVoucherSupplier Management Update Component', () => {
    let comp: CashReceiptVoucherSupplierUpdateComponent;
    let fixture: ComponentFixture<CashReceiptVoucherSupplierUpdateComponent>;
    let service: CashReceiptVoucherSupplierService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [WikunumTestModule],
        declarations: [CashReceiptVoucherSupplierUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(CashReceiptVoucherSupplierUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(CashReceiptVoucherSupplierUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(CashReceiptVoucherSupplierService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new CashReceiptVoucherSupplier(123);
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
        const entity = new CashReceiptVoucherSupplier();
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
