import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { WikunumTestModule } from '../../../test.module';
import { EmployeeTypeUpdateComponent } from 'app/entities/employee-type/employee-type-update.component';
import { EmployeeTypeService } from 'app/entities/employee-type/employee-type.service';
import { EmployeeType } from 'app/shared/model/employee-type.model';

describe('Component Tests', () => {
  describe('EmployeeType Management Update Component', () => {
    let comp: EmployeeTypeUpdateComponent;
    let fixture: ComponentFixture<EmployeeTypeUpdateComponent>;
    let service: EmployeeTypeService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [WikunumTestModule],
        declarations: [EmployeeTypeUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(EmployeeTypeUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(EmployeeTypeUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(EmployeeTypeService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new EmployeeType(123);
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
        const entity = new EmployeeType();
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
