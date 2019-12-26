import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { WikunumTestModule } from '../../../test.module';
import { PaymentTypesUpdateComponent } from 'app/entities/payment-types/payment-types-update.component';
import { PaymentTypesService } from 'app/entities/payment-types/payment-types.service';
import { PaymentTypes } from 'app/shared/model/payment-types.model';

describe('Component Tests', () => {
  describe('PaymentTypes Management Update Component', () => {
    let comp: PaymentTypesUpdateComponent;
    let fixture: ComponentFixture<PaymentTypesUpdateComponent>;
    let service: PaymentTypesService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [WikunumTestModule],
        declarations: [PaymentTypesUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(PaymentTypesUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(PaymentTypesUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(PaymentTypesService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new PaymentTypes(123);
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
        const entity = new PaymentTypes();
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
