import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { WikunumTestModule } from '../../../test.module';
import { ExpenseAccountBalanceUpdateComponent } from 'app/entities/expense-account-balance/expense-account-balance-update.component';
import { ExpenseAccountBalanceService } from 'app/entities/expense-account-balance/expense-account-balance.service';
import { ExpenseAccountBalance } from 'app/shared/model/expense-account-balance.model';

describe('Component Tests', () => {
  describe('ExpenseAccountBalance Management Update Component', () => {
    let comp: ExpenseAccountBalanceUpdateComponent;
    let fixture: ComponentFixture<ExpenseAccountBalanceUpdateComponent>;
    let service: ExpenseAccountBalanceService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [WikunumTestModule],
        declarations: [ExpenseAccountBalanceUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(ExpenseAccountBalanceUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ExpenseAccountBalanceUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ExpenseAccountBalanceService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new ExpenseAccountBalance(123);
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
        const entity = new ExpenseAccountBalance();
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
