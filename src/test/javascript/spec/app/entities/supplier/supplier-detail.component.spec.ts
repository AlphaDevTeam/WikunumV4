import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';
import { JhiDataUtils } from 'ng-jhipster';

import { WikunumTestModule } from '../../../test.module';
import { SupplierDetailComponent } from 'app/entities/supplier/supplier-detail.component';
import { Supplier } from 'app/shared/model/supplier.model';

describe('Component Tests', () => {
  describe('Supplier Management Detail Component', () => {
    let comp: SupplierDetailComponent;
    let fixture: ComponentFixture<SupplierDetailComponent>;
    let dataUtils: JhiDataUtils;
    const route = ({ data: of({ supplier: new Supplier(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [WikunumTestModule],
        declarations: [SupplierDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(SupplierDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(SupplierDetailComponent);
      comp = fixture.componentInstance;
      dataUtils = fixture.debugElement.injector.get(JhiDataUtils);
    });

    describe('OnInit', () => {
      it('Should load supplier on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.supplier).toEqual(jasmine.objectContaining({ id: 123 }));
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
