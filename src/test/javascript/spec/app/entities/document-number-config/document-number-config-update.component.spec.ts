import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { WikunumTestModule } from '../../../test.module';
import { DocumentNumberConfigUpdateComponent } from 'app/entities/document-number-config/document-number-config-update.component';
import { DocumentNumberConfigService } from 'app/entities/document-number-config/document-number-config.service';
import { DocumentNumberConfig } from 'app/shared/model/document-number-config.model';

describe('Component Tests', () => {
  describe('DocumentNumberConfig Management Update Component', () => {
    let comp: DocumentNumberConfigUpdateComponent;
    let fixture: ComponentFixture<DocumentNumberConfigUpdateComponent>;
    let service: DocumentNumberConfigService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [WikunumTestModule],
        declarations: [DocumentNumberConfigUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(DocumentNumberConfigUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(DocumentNumberConfigUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(DocumentNumberConfigService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new DocumentNumberConfig(123);
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
        const entity = new DocumentNumberConfig();
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
