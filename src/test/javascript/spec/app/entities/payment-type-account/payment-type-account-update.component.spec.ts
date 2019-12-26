import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { WikunumTestModule } from '../../../test.module';
import { PaymentTypeAccountUpdateComponent } from 'app/entities/payment-type-account/payment-type-account-update.component';
import { PaymentTypeAccountService } from 'app/entities/payment-type-account/payment-type-account.service';
import { PaymentTypeAccount } from 'app/shared/model/payment-type-account.model';

describe('Component Tests', () => {
  describe('PaymentTypeAccount Management Update Component', () => {
    let comp: PaymentTypeAccountUpdateComponent;
    let fixture: ComponentFixture<PaymentTypeAccountUpdateComponent>;
    let service: PaymentTypeAccountService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [WikunumTestModule],
        declarations: [PaymentTypeAccountUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(PaymentTypeAccountUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(PaymentTypeAccountUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(PaymentTypeAccountService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new PaymentTypeAccount(123);
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
        const entity = new PaymentTypeAccount();
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
