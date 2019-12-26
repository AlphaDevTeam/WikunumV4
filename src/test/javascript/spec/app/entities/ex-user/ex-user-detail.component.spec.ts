import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';
import { JhiDataUtils } from 'ng-jhipster';

import { WikunumTestModule } from '../../../test.module';
import { ExUserDetailComponent } from 'app/entities/ex-user/ex-user-detail.component';
import { ExUser } from 'app/shared/model/ex-user.model';

describe('Component Tests', () => {
  describe('ExUser Management Detail Component', () => {
    let comp: ExUserDetailComponent;
    let fixture: ComponentFixture<ExUserDetailComponent>;
    let dataUtils: JhiDataUtils;
    const route = ({ data: of({ exUser: new ExUser(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [WikunumTestModule],
        declarations: [ExUserDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(ExUserDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ExUserDetailComponent);
      comp = fixture.componentInstance;
      dataUtils = fixture.debugElement.injector.get(JhiDataUtils);
    });

    describe('OnInit', () => {
      it('Should load exUser on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.exUser).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });

    describe('byteSize', () => {
      it('Should call byteSize from JhiDataUtils', () => {
        // GIVEN
        spyOn(dataUtils, 'byteSize');
        const fakeBase64 = 'fake base64';

        // WHEN
        comp.byteSize(fakeBase64);

        // THEN
        expect(dataUtils.byteSize).toBeCalledWith(fakeBase64);
      });
    });

    describe('openFile', () => {
      it('Should call openFile from JhiDataUtils', () => {
        // GIVEN
        spyOn(dataUtils, 'openFile');
        const fakeContentType = 'fake content type';
        const fakeBase64 = 'fake base64';

        // WHEN
        comp.openFile(fakeContentType, fakeBase64);

        // THEN
        expect(dataUtils.openFile).toBeCalledWith(fakeContentType, fakeBase64);
      });
    });
  });
});
