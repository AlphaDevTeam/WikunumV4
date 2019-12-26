import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';
import { JhiDataUtils } from 'ng-jhipster';

import { WikunumTestModule } from '../../../test.module';
import { ItemAddOnsDetailComponent } from 'app/entities/item-add-ons/item-add-ons-detail.component';
import { ItemAddOns } from 'app/shared/model/item-add-ons.model';

describe('Component Tests', () => {
  describe('ItemAddOns Management Detail Component', () => {
    let comp: ItemAddOnsDetailComponent;
    let fixture: ComponentFixture<ItemAddOnsDetailComponent>;
    let dataUtils: JhiDataUtils;
    const route = ({ data: of({ itemAddOns: new ItemAddOns(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [WikunumTestModule],
        declarations: [ItemAddOnsDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(ItemAddOnsDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ItemAddOnsDetailComponent);
      comp = fixture.componentInstance;
      dataUtils = fixture.debugElement.injector.get(JhiDataUtils);
    });

    describe('OnInit', () => {
      it('Should load itemAddOns on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.itemAddOns).toEqual(jasmine.objectContaining({ id: 123 }));
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
