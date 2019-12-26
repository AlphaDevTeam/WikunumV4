import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { take, map } from 'rxjs/operators';
import { PaymentTypesService } from 'app/entities/payment-types/payment-types.service';
import { IPaymentTypes, PaymentTypes } from 'app/shared/model/payment-types.model';

describe('Service Tests', () => {
  describe('PaymentTypes Service', () => {
    let injector: TestBed;
    let service: PaymentTypesService;
    let httpMock: HttpTestingController;
    let elemDefault: IPaymentTypes;
    let expectedResult: IPaymentTypes | IPaymentTypes[] | boolean | null;
    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule]
      });
      expectedResult = null;
      injector = getTestBed();
      service = injector.get(PaymentTypesService);
      httpMock = injector.get(HttpTestingController);

      elemDefault = new PaymentTypes(0, 'AAAAAAA', 'AAAAAAA', 0, false);
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign({}, elemDefault);
        service
          .find(123)
          .pipe(take(1))
          .subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a PaymentTypes', () => {
        const returnedFromService = Object.assign(
          {
            id: 0
          },
          elemDefault
        );
        const expected = Object.assign({}, returnedFromService);
        service
          .create(new PaymentTypes())
          .pipe(take(1))
          .subscribe(resp => (expectedResult = resp.body));
        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a PaymentTypes', () => {
        const returnedFromService = Object.assign(
          {
            paymentTypesCode: 'BBBBBB',
            paymentTypes: 'BBBBBB',
            paymentTypesChargePer: 1,
            isActive: true
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);
        service
          .update(expected)
          .pipe(take(1))
          .subscribe(resp => (expectedResult = resp.body));
        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of PaymentTypes', () => {
        const returnedFromService = Object.assign(
          {
            paymentTypesCode: 'BBBBBB',
            paymentTypes: 'BBBBBB',
            paymentTypesChargePer: 1,
            isActive: true
          },
          elemDefault
        );
        const expected = Object.assign({}, returnedFromService);
        service
          .query()
          .pipe(
            take(1),
            map(resp => resp.body)
          )
          .subscribe(body => (expectedResult = body));
        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a PaymentTypes', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
