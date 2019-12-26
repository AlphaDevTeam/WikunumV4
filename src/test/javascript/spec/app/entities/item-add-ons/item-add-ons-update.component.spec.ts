import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { WikunumTestModule } from '../../../test.module';
import { ItemAddOnsUpdateComponent } from 'app/entities/item-add-ons/item-add-ons-update.component';
import { ItemAddOnsService } from 'app/entities/item-add-ons/item-add-ons.service';
import { ItemAddOns } from 'app/shared/model/item-add-ons.model';

describe('Component Tests', () => {
  describe('ItemAddOns Management Update Component', () => {
    let comp: ItemAddOnsUpdateComponent;
    let fixture: ComponentFixture<ItemAddOnsUpdateComponent>;
    let service: ItemAddOnsService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [WikunumTestModule],
        declarations: [ItemAddOnsUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(ItemAddOnsUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ItemAddOnsUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ItemAddOnsService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new ItemAddOns(123);
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
        const entity = new ItemAddOns();
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
