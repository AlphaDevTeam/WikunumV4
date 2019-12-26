import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { WikunumTestModule } from '../../../test.module';
import { UserPermissionsDetailComponent } from 'app/entities/user-permissions/user-permissions-detail.component';
import { UserPermissions } from 'app/shared/model/user-permissions.model';

describe('Component Tests', () => {
  describe('UserPermissions Management Detail Component', () => {
    let comp: UserPermissionsDetailComponent;
    let fixture: ComponentFixture<UserPermissionsDetailComponent>;
    const route = ({ data: of({ userPermissions: new UserPermissions(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [WikunumTestModule],
        declarations: [UserPermissionsDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(UserPermissionsDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(UserPermissionsDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load userPermissions on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.userPermissions).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
