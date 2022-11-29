import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { WikunumTestModule } from '../../../test.module';
import { CostOfSalesAccountBalanceDetailComponent } from 'app/entities/cost-of-sales-account-balance/cost-of-sales-account-balance-detail.component';
import { CostOfSalesAccountBalance } from 'app/shared/model/cost-of-sales-account-balance.model';

describe('Component Tests', () => {
  describe('CostOfSalesAccountBalance Management Detail Component', () => {
    let comp: CostOfSalesAccountBalanceDetailComponent;
    let fixture: ComponentFixture<CostOfSalesAccountBalanceDetailComponent>;
    const route = ({ data: of({ costOfSalesAccountBalance: new CostOfSalesAccountBalance(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [WikunumTestModule],
        declarations: [CostOfSalesAccountBalanceDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(CostOfSalesAccountBalanceDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(CostOfSalesAccountBalanceDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load costOfSalesAccountBalance on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.costOfSalesAccountBalance).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
