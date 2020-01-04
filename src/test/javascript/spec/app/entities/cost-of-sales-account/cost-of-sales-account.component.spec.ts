import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Data } from '@angular/router';

import { WikunumTestModule } from '../../../test.module';
import { CostOfSalesAccountComponent } from 'app/entities/cost-of-sales-account/cost-of-sales-account.component';
import { CostOfSalesAccountService } from 'app/entities/cost-of-sales-account/cost-of-sales-account.service';
import { CostOfSalesAccount } from 'app/shared/model/cost-of-sales-account.model';

describe('Component Tests', () => {
  describe('CostOfSalesAccount Management Component', () => {
    let comp: CostOfSalesAccountComponent;
    let fixture: ComponentFixture<CostOfSalesAccountComponent>;
    let service: CostOfSalesAccountService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [WikunumTestModule],
        declarations: [CostOfSalesAccountComponent],
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
        .overrideTemplate(CostOfSalesAccountComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(CostOfSalesAccountComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(CostOfSalesAccountService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new CostOfSalesAccount(123)],
            headers
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.costOfSalesAccounts && comp.costOfSalesAccounts[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });

    it('should load a page', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new CostOfSalesAccount(123)],
            headers
          })
        )
      );

      // WHEN
      comp.loadPage(1);

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.costOfSalesAccounts && comp.costOfSalesAccounts[0]).toEqual(jasmine.objectContaining({ id: 123 }));
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
