import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { WikunumTestModule } from '../../../test.module';
import { LicenseTypeUpdateComponent } from 'app/entities/license-type/license-type-update.component';
import { LicenseTypeService } from 'app/entities/license-type/license-type.service';
import { LicenseType } from 'app/shared/model/license-type.model';

describe('Component Tests', () => {
  describe('LicenseType Management Update Component', () => {
    let comp: LicenseTypeUpdateComponent;
    let fixture: ComponentFixture<LicenseTypeUpdateComponent>;
    let service: LicenseTypeService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [WikunumTestModule],
        declarations: [LicenseTypeUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(LicenseTypeUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(LicenseTypeUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(LicenseTypeService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new LicenseType(123);
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
        const entity = new LicenseType();
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
