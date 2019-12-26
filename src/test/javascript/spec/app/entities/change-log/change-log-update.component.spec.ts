import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { WikunumTestModule } from '../../../test.module';
import { ChangeLogUpdateComponent } from 'app/entities/change-log/change-log-update.component';
import { ChangeLogService } from 'app/entities/change-log/change-log.service';
import { ChangeLog } from 'app/shared/model/change-log.model';

describe('Component Tests', () => {
  describe('ChangeLog Management Update Component', () => {
    let comp: ChangeLogUpdateComponent;
    let fixture: ComponentFixture<ChangeLogUpdateComponent>;
    let service: ChangeLogService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [WikunumTestModule],
        declarations: [ChangeLogUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(ChangeLogUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ChangeLogUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ChangeLogService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new ChangeLog(123);
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
        const entity = new ChangeLog();
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
