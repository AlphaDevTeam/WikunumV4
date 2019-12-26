import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { WikunumTestModule } from '../../../test.module';
import { PaymentTypeBalanceDetailComponent } from 'app/entities/payment-type-balance/payment-type-balance-detail.component';
import { PaymentTypeBalance } from 'app/shared/model/payment-type-balance.model';

describe('Component Tests', () => {
  describe('PaymentTypeBalance Management Detail Component', () => {
    let comp: PaymentTypeBalanceDetailComponent;
    let fixture: ComponentFixture<PaymentTypeBalanceDetailComponent>;
    const route = ({ data: of({ paymentTypeBalance: new PaymentTypeBalance(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [WikunumTestModule],
        declarations: [PaymentTypeBalanceDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(PaymentTypeBalanceDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(PaymentTypeBalanceDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load paymentTypeBalance on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.paymentTypeBalance).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
