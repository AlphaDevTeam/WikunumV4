import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { WikunumTestModule } from '../../../test.module';
import { ConfigurationItemsUpdateComponent } from 'app/entities/configuration-items/configuration-items-update.component';
import { ConfigurationItemsService } from 'app/entities/configuration-items/configuration-items.service';
import { ConfigurationItems } from 'app/shared/model/configuration-items.model';

describe('Component Tests', () => {
  describe('ConfigurationItems Management Update Component', () => {
    let comp: ConfigurationItemsUpdateComponent;
    let fixture: ComponentFixture<ConfigurationItemsUpdateComponent>;
    let service: ConfigurationItemsService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [WikunumTestModule],
        declarations: [ConfigurationItemsUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(ConfigurationItemsUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ConfigurationItemsUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ConfigurationItemsService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new ConfigurationItems(123);
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
        const entity = new ConfigurationItems();
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
