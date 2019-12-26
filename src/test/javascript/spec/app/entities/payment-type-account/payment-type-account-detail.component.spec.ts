import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { WikunumTestModule } from '../../../test.module';
import { PaymentTypeAccountDetailComponent } from 'app/entities/payment-type-account/payment-type-account-detail.component';
import { PaymentTypeAccount } from 'app/shared/model/payment-type-account.model';

describe('Component Tests', () => {
  describe('PaymentTypeAccount Management Detail Component', () => {
    let comp: PaymentTypeAccountDetailComponent;
    let fixture: ComponentFixture<PaymentTypeAccountDetailComponent>;
    const route = ({ data: of({ paymentTypeAccount: new PaymentTypeAccount(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [WikunumTestModule],
        declarations: [PaymentTypeAccountDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(PaymentTypeAccountDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(PaymentTypeAccountDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load paymentTypeAccount on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.paymentTypeAccount).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
