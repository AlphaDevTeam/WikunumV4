import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { WikunumTestModule } from '../../../test.module';
import { GoodsReceiptDetailsDetailComponent } from 'app/entities/goods-receipt-details/goods-receipt-details-detail.component';
import { GoodsReceiptDetails } from 'app/shared/model/goods-receipt-details.model';

describe('Component Tests', () => {
  describe('GoodsReceiptDetails Management Detail Component', () => {
    let comp: GoodsReceiptDetailsDetailComponent;
    let fixture: ComponentFixture<GoodsReceiptDetailsDetailComponent>;
    const route = ({ data: of({ goodsReceiptDetails: new GoodsReceiptDetails(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [WikunumTestModule],
        declarations: [GoodsReceiptDetailsDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(GoodsReceiptDetailsDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(GoodsReceiptDetailsDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load goodsReceiptDetails on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.goodsReceiptDetails).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
