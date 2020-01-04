import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { WikunumTestModule } from '../../../test.module';
import { MenuItemsUpdateComponent } from 'app/entities/menu-items/menu-items-update.component';
import { MenuItemsService } from 'app/entities/menu-items/menu-items.service';
import { MenuItems } from 'app/shared/model/menu-items.model';

describe('Component Tests', () => {
  describe('MenuItems Management Update Component', () => {
    let comp: MenuItemsUpdateComponent;
    let fixture: ComponentFixture<MenuItemsUpdateComponent>;
    let service: MenuItemsService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [WikunumTestModule],
        declarations: [MenuItemsUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(MenuItemsUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(MenuItemsUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(MenuItemsService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new MenuItems(123);
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
        const entity = new MenuItems();
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
