import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { WikunumTestModule } from '../../../test.module';
import { CostOfSalesAccountUpdateComponent } from 'app/entities/cost-of-sales-account/cost-of-sales-account-update.component';
import { CostOfSalesAccountService } from 'app/entities/cost-of-sales-account/cost-of-sales-account.service';
import { CostOfSalesAccount } from 'app/shared/model/cost-of-sales-account.model';

describe('Component Tests', () => {
  describe('CostOfSalesAccount Management Update Component', () => {
    let comp: CostOfSalesAccountUpdateComponent;
    let fixture: ComponentFixture<CostOfSalesAccountUpdateComponent>;
    let service: CostOfSalesAccountService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [WikunumTestModule],
        declarations: [CostOfSalesAccountUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(CostOfSalesAccountUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(CostOfSalesAccountUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(CostOfSalesAccountService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new CostOfSalesAccount(123);
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
        const entity = new CostOfSalesAccount();
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
