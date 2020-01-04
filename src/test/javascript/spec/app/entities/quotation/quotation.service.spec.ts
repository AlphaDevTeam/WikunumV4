import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { take, map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { QuotationService } from 'app/entities/quotation/quotation.service';
import { IQuotation, Quotation } from 'app/shared/model/quotation.model';

describe('Service Tests', () => {
  describe('Quotation Service', () => {
    let injector: TestBed;
    let service: QuotationService;
    let httpMock: HttpTestingController;
    let elemDefault: IQuotation;
    let expectedResult: IQuotation | IQuotation[] | boolean | null;
    let currentDate: moment.Moment;
    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule]
      });
      expectedResult = null;
      injector = getTestBed();
      service = injector.get(QuotationService);
      httpMock = injector.get(HttpTestingController);
      currentDate = moment();

      elemDefault = new Quotation(0, 'AAAAAAA', currentDate, currentDate, 0, 'AAAAAAA', 'AAAAAAA', 'AAAAAAA', 'AAAAAAA');
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            quotationDate: currentDate.format(DATE_FORMAT),
            quotationexpireDate: currentDate.format(DATE_FORMAT)
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

      it('should create a Quotation', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            quotationDate: currentDate.format(DATE_FORMAT),
            quotationexpireDate: currentDate.format(DATE_FORMAT)
          },
          elemDefault
        );
        const expected = Object.assign(
          {
            quotationDate: currentDate,
            quotationexpireDate: currentDate
          },
          returnedFromService
        );
        service
          .create(new Quotation())
          .pipe(take(1))
          .subscribe(resp => (expectedResult = resp.body));
        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Quotation', () => {
        const returnedFromService = Object.assign(
          {
            quotationNumber: 'BBBBBB',
            quotationDate: currentDate.format(DATE_FORMAT),
            quotationexpireDate: currentDate.format(DATE_FORMAT),
            quotationTotalAmount: 1,
            quotationTo: 'BBBBBB',
            quotationFrom: 'BBBBBB',
            projectNumber: 'BBBBBB',
            quotationNote: 'BBBBBB'
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            quotationDate: currentDate,
            quotationexpireDate: currentDate
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

      it('should return a list of Quotation', () => {
        const returnedFromService = Object.assign(
          {
            quotationNumber: 'BBBBBB',
            quotationDate: currentDate.format(DATE_FORMAT),
            quotationexpireDate: currentDate.format(DATE_FORMAT),
            quotationTotalAmount: 1,
            quotationTo: 'BBBBBB',
            quotationFrom: 'BBBBBB',
            projectNumber: 'BBBBBB',
            quotationNote: 'BBBBBB'
          },
          elemDefault
        );
        const expected = Object.assign(
          {
            quotationDate: currentDate,
            quotationexpireDate: currentDate
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

      it('should delete a Quotation', () => {
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
