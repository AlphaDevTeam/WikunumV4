import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { WikunumTestModule } from '../../../test.module';
import { PurchaseAccountUpdateComponent } from 'app/entities/purchase-account/purchase-account-update.component';
import { PurchaseAccountService } from 'app/entities/purchase-account/purchase-account.service';
import { PurchaseAccount } from 'app/shared/model/purchase-account.model';

describe('Component Tests', () => {
  describe('PurchaseAccount Management Update Component', () => {
    let comp: PurchaseAccountUpdateComponent;
    let fixture: ComponentFixture<PurchaseAccountUpdateComponent>;
    let service: PurchaseAccountService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [WikunumTestModule],
        declarations: [PurchaseAccountUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(PurchaseAccountUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(PurchaseAccountUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(PurchaseAccountService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new PurchaseAccount(123);
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
        const entity = new PurchaseAccount();
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
