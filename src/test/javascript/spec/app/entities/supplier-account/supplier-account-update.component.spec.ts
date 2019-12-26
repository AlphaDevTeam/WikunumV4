import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { WikunumTestModule } from '../../../test.module';
import { SupplierAccountUpdateComponent } from 'app/entities/supplier-account/supplier-account-update.component';
import { SupplierAccountService } from 'app/entities/supplier-account/supplier-account.service';
import { SupplierAccount } from 'app/shared/model/supplier-account.model';

describe('Component Tests', () => {
  describe('SupplierAccount Management Update Component', () => {
    let comp: SupplierAccountUpdateComponent;
    let fixture: ComponentFixture<SupplierAccountUpdateComponent>;
    let service: SupplierAccountService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [WikunumTestModule],
        declarations: [SupplierAccountUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(SupplierAccountUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(SupplierAccountUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(SupplierAccountService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new SupplierAccount(123);
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
        const entity = new SupplierAccount();
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
