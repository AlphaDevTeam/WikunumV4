import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { WikunumTestModule } from '../../../test.module';
import { DocumentHistoryUpdateComponent } from 'app/entities/document-history/document-history-update.component';
import { DocumentHistoryService } from 'app/entities/document-history/document-history.service';
import { DocumentHistory } from 'app/shared/model/document-history.model';

describe('Component Tests', () => {
  describe('DocumentHistory Management Update Component', () => {
    let comp: DocumentHistoryUpdateComponent;
    let fixture: ComponentFixture<DocumentHistoryUpdateComponent>;
    let service: DocumentHistoryService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [WikunumTestModule],
        declarations: [DocumentHistoryUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(DocumentHistoryUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(DocumentHistoryUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(DocumentHistoryService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new DocumentHistory(123);
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
        const entity = new DocumentHistory();
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
