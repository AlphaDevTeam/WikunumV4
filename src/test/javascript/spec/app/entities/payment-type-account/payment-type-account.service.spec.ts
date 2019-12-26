import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { take, map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { PaymentTypeAccountService } from 'app/entities/payment-type-account/payment-type-account.service';
import { IPaymentTypeAccount, PaymentTypeAccount } from 'app/shared/model/payment-type-account.model';

describe('Service Tests', () => {
  describe('PaymentTypeAccount Service', () => {
    let injector: TestBed;
    let service: PaymentTypeAccountService;
    let httpMock: HttpTestingController;
    let elemDefault: IPaymentTypeAccount;
    let expectedResult: IPaymentTypeAccount | IPaymentTypeAccount[] | boolean | null;
    let currentDate: moment.Moment;
    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule]
      });
      expectedResult = null;
      injector = getTestBed();
      service = injector.get(PaymentTypeAccountService);
      httpMock = injector.get(HttpTestingController);
      currentDate = moment();

      elemDefault = new PaymentTypeAccount(0, currentDate, 'AAAAAAA', 0, 0);
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            transactionDate: currentDate.format(DATE_FORMAT)
          },
          elemDefault
        );
        service
          .find(123)
          .pipe(take(1))
          .subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a PaymentTypeAccount', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            transactionDate: currentDate.format(DATE_FORMAT)
          },
          elemDefault
        );
        const expected = Object.assign(
          {
            transactionDate: currentDate
          },
          returnedFromService
        );
        service
          .create(new PaymentTypeAccount())
          .pipe(take(1))
          .subscribe(resp => (expectedResult = resp.body));
        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a PaymentTypeAccount', () => {
        const returnedFromService = Object.assign(
          {
            transactionDate: currentDate.format(DATE_FORMAT),
            transactionDescription: 'BBBBBB',
            transactionAmount: 1,
            transactionBalance: 1
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            transactionDate: currentDate
          },
          returnedFromService
        );
        service
          .update(expected)
          .pipe(take(1))
          .subscribe(resp => (expectedResult = resp.body));
        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of PaymentTypeAccount', () => {
        const returnedFromService = Object.assign(
          {
            transactionDate: currentDate.format(DATE_FORMAT),
            transactionDescription: 'BBBBBB',
            transactionAmount: 1,
            transactionBalance: 1
          },
          elemDefault
        );
        const expected = Object.assign(
          {
            transactionDate: currentDate
          },
          returnedFromService
        );
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

      it('should delete a PaymentTypeAccount', () => {
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
