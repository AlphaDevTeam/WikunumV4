import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { WikunumTestModule } from '../../../test.module';
import { EmployeeAccountBalanceDetailComponent } from 'app/entities/employee-account-balance/employee-account-balance-detail.component';
import { EmployeeAccountBalance } from 'app/shared/model/employee-account-balance.model';

describe('Component Tests', () => {
  describe('EmployeeAccountBalance Management Detail Component', () => {
    let comp: EmployeeAccountBalanceDetailComponent;
    let fixture: ComponentFixture<EmployeeAccountBalanceDetailComponent>;
    const route = ({ data: of({ employeeAccountBalance: new EmployeeAccountBalance(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [WikunumTestModule],
        declarations: [EmployeeAccountBalanceDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(EmployeeAccountBalanceDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(EmployeeAccountBalanceDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load employeeAccountBalance on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.employeeAccountBalance).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
