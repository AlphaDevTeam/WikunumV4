import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { take, map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { CashPaymentVoucherExpenseService } from 'app/entities/cash-payment-voucher-expense/cash-payment-voucher-expense.service';
import { ICashPaymentVoucherExpense, CashPaymentVoucherExpense } from 'app/shared/model/cash-payment-voucher-expense.model';

describe('Service Tests', () => {
  describe('CashPaymentVoucherExpense Service', () => {
    let injector: TestBed;
    let service: CashPaymentVoucherExpenseService;
    let httpMock: HttpTestingController;
    let elemDefault: ICashPaymentVoucherExpense;
    let expectedResult: ICashPaymentVoucherExpense | ICashPaymentVoucherExpense[] | boolean | null;
    let currentDate: moment.Moment;
    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule]
      });
      expectedResult = null;
      injector = getTestBed();
      service = injector.get(CashPaymentVoucherExpenseService);
      httpMock = injector.get(HttpTestingController);
      currentDate = moment();

      elemDefault = new CashPaymentVoucherExpense(0, 'AAAAAAA', currentDate, 'AAAAAAA', 0);
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

      it('should create a CashPaymentVoucherExpense', () => {
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
          .create(new CashPaymentVoucherExpense())
          .pipe(take(1))
          .subscribe(resp => (expectedResult = resp.body));
        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a CashPaymentVoucherExpense', () => {
        const returnedFromService = Object.assign(
          {
            transactionNumber: 'BBBBBB',
            transactionDate: currentDate.format(DATE_FORMAT),
            transactionDescription: 'BBBBBB',
            transactionAmount: 1
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

      it('should return a list of CashPaymentVoucherExpense', () => {
        const returnedFromService = Object.assign(
          {
            transactionNumber: 'BBBBBB',
            transactionDate: currentDate.format(DATE_FORMAT),
            transactionDescription: 'BBBBBB',
            transactionAmount: 1
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

      it('should delete a CashPaymentVoucherExpense', () => {
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
