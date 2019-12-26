import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { WikunumTestModule } from '../../../test.module';
import { PurchaseAccountBalanceDetailComponent } from 'app/entities/purchase-account-balance/purchase-account-balance-detail.component';
import { PurchaseAccountBalance } from 'app/shared/model/purchase-account-balance.model';

describe('Component Tests', () => {
  describe('PurchaseAccountBalance Management Detail Component', () => {
    let comp: PurchaseAccountBalanceDetailComponent;
    let fixture: ComponentFixture<PurchaseAccountBalanceDetailComponent>;
    const route = ({ data: of({ purchaseAccountBalance: new PurchaseAccountBalance(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [WikunumTestModule],
        declarations: [PurchaseAccountBalanceDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(PurchaseAccountBalanceDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(PurchaseAccountBalanceDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load purchaseAccountBalance on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.purchaseAccountBalance).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
