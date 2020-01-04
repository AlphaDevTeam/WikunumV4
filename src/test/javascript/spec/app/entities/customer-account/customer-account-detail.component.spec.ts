import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { WikunumTestModule } from '../../../test.module';
import { CustomerAccountDetailComponent } from 'app/entities/customer-account/customer-account-detail.component';
import { CustomerAccount } from 'app/shared/model/customer-account.model';

describe('Component Tests', () => {
  describe('CustomerAccount Management Detail Component', () => {
    let comp: CustomerAccountDetailComponent;
    let fixture: ComponentFixture<CustomerAccountDetailComponent>;
    const route = ({ data: of({ customerAccount: new CustomerAccount(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [WikunumTestModule],
        declarations: [CustomerAccountDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(CustomerAccountDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(CustomerAccountDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load customerAccount on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.customerAccount).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
