import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { WikunumTestModule } from '../../../test.module';
import { EmployeeAccountBalanceUpdateComponent } from 'app/entities/employee-account-balance/employee-account-balance-update.component';
import { EmployeeAccountBalanceService } from 'app/entities/employee-account-balance/employee-account-balance.service';
import { EmployeeAccountBalance } from 'app/shared/model/employee-account-balance.model';

describe('Component Tests', () => {
  describe('EmployeeAccountBalance Management Update Component', () => {
    let comp: EmployeeAccountBalanceUpdateComponent;
    let fixture: ComponentFixture<EmployeeAccountBalanceUpdateComponent>;
    let service: EmployeeAccountBalanceService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [WikunumTestModule],
        declarations: [EmployeeAccountBalanceUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(EmployeeAccountBalanceUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(EmployeeAccountBalanceUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(EmployeeAccountBalanceService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new EmployeeAccountBalance(123);
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
        const entity = new EmployeeAccountBalance();
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
