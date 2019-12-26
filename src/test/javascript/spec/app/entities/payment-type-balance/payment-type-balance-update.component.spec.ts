import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { WikunumTestModule } from '../../../test.module';
import { PaymentTypeBalanceUpdateComponent } from 'app/entities/payment-type-balance/payment-type-balance-update.component';
import { PaymentTypeBalanceService } from 'app/entities/payment-type-balance/payment-type-balance.service';
import { PaymentTypeBalance } from 'app/shared/model/payment-type-balance.model';

describe('Component Tests', () => {
  describe('PaymentTypeBalance Management Update Component', () => {
    let comp: PaymentTypeBalanceUpdateComponent;
    let fixture: ComponentFixture<PaymentTypeBalanceUpdateComponent>;
    let service: PaymentTypeBalanceService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [WikunumTestModule],
        declarations: [PaymentTypeBalanceUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(PaymentTypeBalanceUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(PaymentTypeBalanceUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(PaymentTypeBalanceService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new PaymentTypeBalance(123);
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
        const entity = new PaymentTypeBalance();
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
