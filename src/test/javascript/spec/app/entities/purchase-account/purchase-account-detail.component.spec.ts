import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { WikunumTestModule } from '../../../test.module';
import { PurchaseAccountDetailComponent } from 'app/entities/purchase-account/purchase-account-detail.component';
import { PurchaseAccount } from 'app/shared/model/purchase-account.model';

describe('Component Tests', () => {
  describe('PurchaseAccount Management Detail Component', () => {
    let comp: PurchaseAccountDetailComponent;
    let fixture: ComponentFixture<PurchaseAccountDetailComponent>;
    const route = ({ data: of({ purchaseAccount: new PurchaseAccount(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [WikunumTestModule],
        declarations: [PurchaseAccountDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(PurchaseAccountDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(PurchaseAccountDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load purchaseAccount on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.purchaseAccount).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
