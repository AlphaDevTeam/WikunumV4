import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { WikunumTestModule } from '../../../test.module';
import { PaymentTypesDetailComponent } from 'app/entities/payment-types/payment-types-detail.component';
import { PaymentTypes } from 'app/shared/model/payment-types.model';

describe('Component Tests', () => {
  describe('PaymentTypes Management Detail Component', () => {
    let comp: PaymentTypesDetailComponent;
    let fixture: ComponentFixture<PaymentTypesDetailComponent>;
    const route = ({ data: of({ paymentTypes: new PaymentTypes(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [WikunumTestModule],
        declarations: [PaymentTypesDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(PaymentTypesDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(PaymentTypesDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load paymentTypes on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.paymentTypes).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
