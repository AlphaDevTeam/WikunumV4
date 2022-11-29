import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { WikunumTestModule } from '../../../test.module';
import { StockTransferDetailComponent } from 'app/entities/stock-transfer/stock-transfer-detail.component';
import { StockTransfer } from 'app/shared/model/stock-transfer.model';

describe('Component Tests', () => {
  describe('StockTransfer Management Detail Component', () => {
    let comp: StockTransferDetailComponent;
    let fixture: ComponentFixture<StockTransferDetailComponent>;
    const route = ({ data: of({ stockTransfer: new StockTransfer(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [WikunumTestModule],
        declarations: [StockTransferDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(StockTransferDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(StockTransferDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load stockTransfer on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.stockTransfer).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
