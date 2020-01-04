import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Data } from '@angular/router';

import { WikunumTestModule } from '../../../test.module';
import { SupplierAccountBalanceComponent } from 'app/entities/supplier-account-balance/supplier-account-balance.component';
import { SupplierAccountBalanceService } from 'app/entities/supplier-account-balance/supplier-account-balance.service';
import { SupplierAccountBalance } from 'app/shared/model/supplier-account-balance.model';

describe('Component Tests', () => {
  describe('SupplierAccountBalance Management Component', () => {
    let comp: SupplierAccountBalanceComponent;
    let fixture: ComponentFixture<SupplierAccountBalanceComponent>;
    let service: SupplierAccountBalanceService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [WikunumTestModule],
        declarations: [SupplierAccountBalanceComponent],
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
        .overrideTemplate(SupplierAccountBalanceComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(SupplierAccountBalanceComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(SupplierAccountBalanceService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new SupplierAccountBalance(123)],
            headers
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.supplierAccountBalances && comp.supplierAccountBalances[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });

    it('should load a page', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new SupplierAccountBalance(123)],
            headers
          })
        )
      );

      // WHEN
      comp.loadPage(1);

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.supplierAccountBalances && comp.supplierAccountBalances[0]).toEqual(jasmine.objectContaining({ id: 123 }));
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
