import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { WikunumTestModule } from '../../../test.module';
import { CostOfSalesAccountBalanceUpdateComponent } from 'app/entities/cost-of-sales-account-balance/cost-of-sales-account-balance-update.component';
import { CostOfSalesAccountBalanceService } from 'app/entities/cost-of-sales-account-balance/cost-of-sales-account-balance.service';
import { CostOfSalesAccountBalance } from 'app/shared/model/cost-of-sales-account-balance.model';

describe('Component Tests', () => {
  describe('CostOfSalesAccountBalance Management Update Component', () => {
    let comp: CostOfSalesAccountBalanceUpdateComponent;
    let fixture: ComponentFixture<CostOfSalesAccountBalanceUpdateComponent>;
    let service: CostOfSalesAccountBalanceService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [WikunumTestModule],
        declarations: [CostOfSalesAccountBalanceUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(CostOfSalesAccountBalanceUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(CostOfSalesAccountBalanceUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(CostOfSalesAccountBalanceService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new CostOfSalesAccountBalance(123);
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
        const entity = new CostOfSalesAccountBalance();
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
