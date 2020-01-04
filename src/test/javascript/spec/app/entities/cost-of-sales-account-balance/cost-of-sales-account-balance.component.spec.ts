import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Data } from '@angular/router';

import { WikunumTestModule } from '../../../test.module';
import { CostOfSalesAccountBalanceComponent } from 'app/entities/cost-of-sales-account-balance/cost-of-sales-account-balance.component';
import { CostOfSalesAccountBalanceService } from 'app/entities/cost-of-sales-account-balance/cost-of-sales-account-balance.service';
import { CostOfSalesAccountBalance } from 'app/shared/model/cost-of-sales-account-balance.model';

describe('Component Tests', () => {
  describe('CostOfSalesAccountBalance Management Component', () => {
    let comp: CostOfSalesAccountBalanceComponent;
    let fixture: ComponentFixture<CostOfSalesAccountBalanceComponent>;
    let service: CostOfSalesAccountBalanceService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [WikunumTestModule],
        declarations: [CostOfSalesAccountBalanceComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: {
              data: {
                subscribe: (fn: (value: Data) => void) =>
                  fn({
                    pagingParams: {
                      predicate: 'id',
                      reverse: false,
                      page: 0
                    }
                  })
              }
            }
          }
        ]
      })
        .overrideTemplate(CostOfSalesAccountBalanceComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(CostOfSalesAccountBalanceComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(CostOfSalesAccountBalanceService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new CostOfSalesAccountBalance(123)],
            headers
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.costOfSalesAccountBalances && comp.costOfSalesAccountBalances[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });

    it('should load a page', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new CostOfSalesAccountBalance(123)],
            headers
          })
        )
      );

      // WHEN
      comp.loadPage(1);

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.costOfSalesAccountBalances && comp.costOfSalesAccountBalances[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });

    it('should calculate the sort attribute for an id', () => {
      // WHEN
      comp.ngOnInit();
      const result = comp.sort();

      // THEN
      expect(result).toEqual(['id,desc']);
    });

    it('should calculate the sort attribute for a non-id attribute', () => {
      // INIT
      comp.ngOnInit();

      // GIVEN
      comp.predicate = 'name';

      // WHEN
      const result = comp.sort();

      // THEN
      expect(result).toEqual(['name,desc', 'id']);
    });
  });
});
