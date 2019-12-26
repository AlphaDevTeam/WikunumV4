import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { WikunumTestModule } from '../../../test.module';
import { ExpenseAccountUpdateComponent } from 'app/entities/expense-account/expense-account-update.component';
import { ExpenseAccountService } from 'app/entities/expense-account/expense-account.service';
import { ExpenseAccount } from 'app/shared/model/expense-account.model';

describe('Component Tests', () => {
  describe('ExpenseAccount Management Update Component', () => {
    let comp: ExpenseAccountUpdateComponent;
    let fixture: ComponentFixture<ExpenseAccountUpdateComponent>;
    let service: ExpenseAccountService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [WikunumTestModule],
        declarations: [ExpenseAccountUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(ExpenseAccountUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ExpenseAccountUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ExpenseAccountService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new ExpenseAccount(123);
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
        const entity = new ExpenseAccount();
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
