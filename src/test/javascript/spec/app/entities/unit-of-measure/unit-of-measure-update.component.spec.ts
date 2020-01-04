import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { WikunumTestModule } from '../../../test.module';
import { UnitOfMeasureUpdateComponent } from 'app/entities/unit-of-measure/unit-of-measure-update.component';
import { UnitOfMeasureService } from 'app/entities/unit-of-measure/unit-of-measure.service';
import { UnitOfMeasure } from 'app/shared/model/unit-of-measure.model';

describe('Component Tests', () => {
  describe('UnitOfMeasure Management Update Component', () => {
    let comp: UnitOfMeasureUpdateComponent;
    let fixture: ComponentFixture<UnitOfMeasureUpdateComponent>;
    let service: UnitOfMeasureService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [WikunumTestModule],
        declarations: [UnitOfMeasureUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(UnitOfMeasureUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(UnitOfMeasureUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(UnitOfMeasureService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new UnitOfMeasure(123);
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
        const entity = new UnitOfMeasure();
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
