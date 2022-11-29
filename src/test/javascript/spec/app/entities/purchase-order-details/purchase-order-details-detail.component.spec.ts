import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { WikunumTestModule } from '../../../test.module';
import { PurchaseOrderDetailsDetailComponent } from 'app/entities/purchase-order-details/purchase-order-details-detail.component';
import { PurchaseOrderDetails } from 'app/shared/model/purchase-order-details.model';

describe('Component Tests', () => {
  describe('PurchaseOrderDetails Management Detail Component', () => {
    let comp: PurchaseOrderDetailsDetailComponent;
    let fixture: ComponentFixture<PurchaseOrderDetailsDetailComponent>;
    const route = ({ data: of({ purchaseOrderDetails: new PurchaseOrderDetails(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [WikunumTestModule],
        declarations: [PurchaseOrderDetailsDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(PurchaseOrderDetailsDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(PurchaseOrderDetailsDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load purchaseOrderDetails on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.purchaseOrderDetails).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
