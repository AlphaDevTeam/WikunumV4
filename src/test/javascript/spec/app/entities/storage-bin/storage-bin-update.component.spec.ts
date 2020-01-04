import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { WikunumTestModule } from '../../../test.module';
import { StorageBinUpdateComponent } from 'app/entities/storage-bin/storage-bin-update.component';
import { StorageBinService } from 'app/entities/storage-bin/storage-bin.service';
import { StorageBin } from 'app/shared/model/storage-bin.model';

describe('Component Tests', () => {
  describe('StorageBin Management Update Component', () => {
    let comp: StorageBinUpdateComponent;
    let fixture: ComponentFixture<StorageBinUpdateComponent>;
    let service: StorageBinService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [WikunumTestModule],
        declarations: [StorageBinUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(StorageBinUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(StorageBinUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(StorageBinService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new StorageBin(123);
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
        const entity = new StorageBin();
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
