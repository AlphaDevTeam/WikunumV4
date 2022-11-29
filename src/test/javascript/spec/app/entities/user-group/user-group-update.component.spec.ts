import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { WikunumTestModule } from '../../../test.module';
import { UserGroupUpdateComponent } from 'app/entities/user-group/user-group-update.component';
import { UserGroupService } from 'app/entities/user-group/user-group.service';
import { UserGroup } from 'app/shared/model/user-group.model';

describe('Component Tests', () => {
  describe('UserGroup Management Update Component', () => {
    let comp: UserGroupUpdateComponent;
    let fixture: ComponentFixture<UserGroupUpdateComponent>;
    let service: UserGroupService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [WikunumTestModule],
        declarations: [UserGroupUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(UserGroupUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(UserGroupUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(UserGroupService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new UserGroup(123);
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
        const entity = new UserGroup();
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
