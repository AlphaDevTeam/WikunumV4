import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { WikunumTestModule } from '../../../test.module';
import { CashBookBalanceDetailComponent } from 'app/entities/cash-book-balance/cash-book-balance-detail.component';
import { CashBookBalance } from 'app/shared/model/cash-book-balance.model';

describe('Component Tests', () => {
  describe('CashBookBalance Management Detail Component', () => {
    let comp: CashBookBalanceDetailComponent;
    let fixture: ComponentFixture<CashBookBalanceDetailComponent>;
    const route = ({ data: of({ cashBookBalance: new CashBookBalance(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [WikunumTestModule],
        declarations: [CashBookBalanceDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(CashBookBalanceDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(CashBookBalanceDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load cashBookBalance on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.cashBookBalance).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
