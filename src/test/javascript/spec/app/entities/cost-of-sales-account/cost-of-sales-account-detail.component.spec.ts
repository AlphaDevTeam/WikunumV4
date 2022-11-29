import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { WikunumTestModule } from '../../../test.module';
import { CostOfSalesAccountDetailComponent } from 'app/entities/cost-of-sales-account/cost-of-sales-account-detail.component';
import { CostOfSalesAccount } from 'app/shared/model/cost-of-sales-account.model';

describe('Component Tests', () => {
  describe('CostOfSalesAccount Management Detail Component', () => {
    let comp: CostOfSalesAccountDetailComponent;
    let fixture: ComponentFixture<CostOfSalesAccountDetailComponent>;
    const route = ({ data: of({ costOfSalesAccount: new CostOfSalesAccount(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [WikunumTestModule],
        declarations: [CostOfSalesAccountDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(CostOfSalesAccountDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(CostOfSalesAccountDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load costOfSalesAccount on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.costOfSalesAccount).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
