import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { WikunumTestModule } from '../../../test.module';
import { SupplierAccountBalanceDetailComponent } from 'app/entities/supplier-account-balance/supplier-account-balance-detail.component';
import { SupplierAccountBalance } from 'app/shared/model/supplier-account-balance.model';

describe('Component Tests', () => {
  describe('SupplierAccountBalance Management Detail Component', () => {
    let comp: SupplierAccountBalanceDetailComponent;
    let fixture: ComponentFixture<SupplierAccountBalanceDetailComponent>;
    const route = ({ data: of({ supplierAccountBalance: new SupplierAccountBalance(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [WikunumTestModule],
        declarations: [SupplierAccountBalanceDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(SupplierAccountBalanceDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(SupplierAccountBalanceDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load supplierAccountBalance on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.supplierAccountBalance).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
