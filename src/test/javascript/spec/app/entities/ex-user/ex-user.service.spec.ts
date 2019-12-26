import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { take, map } from 'rxjs/operators';
import { ExUserService } from 'app/entities/ex-user/ex-user.service';
import { IExUser, ExUser } from 'app/shared/model/ex-user.model';

describe('Service Tests', () => {
  describe('ExUser Service', () => {
    let injector: TestBed;
    let service: ExUserService;
    let httpMock: HttpTestingController;
    let elemDefault: IExUser;
    let expectedResult: IExUser | IExUser[] | boolean | null;
    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule]
      });
      expectedResult = null;
      injector = getTestBed();
      service = injector.get(ExUserService);
      httpMock = injector.get(HttpTestingController);

      elemDefault = new ExUser(
        0,
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        false,
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'image/png',
        'AAAAAAA',
        0,
        0
      );
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

      it('should create a ExUser', () => {
        const returnedFromService = Object.assign(
          {
            id: 0
          },
          elemDefault
        );
        const expected = Object.assign({}, returnedFromService);
        service
          .create(new ExUser())
          .pipe(take(1))
          .subscribe(resp => (expectedResult = resp.body));
        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a ExUser', () => {
        const returnedFromService = Object.assign(
          {
            userKey: 'BBBBBB',
            login: 'BBBBBB',
            firstName: 'BBBBBB',
            lastName: 'BBBBBB',
            email: 'BBBBBB',
            isActive: true,
            phone: 'BBBBBB',
            addressLine1: 'BBBBBB',
            addressLine2: 'BBBBBB',
            city: 'BBBBBB',
            country: 'BBBBBB',
            image: 'BBBBBB',
            userLimit: 1,
            creditScore: 1
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

      it('should return a list of ExUser', () => {
        const returnedFromService = Object.assign(
          {
            userKey: 'BBBBBB',
            login: 'BBBBBB',
            firstName: 'BBBBBB',
            lastName: 'BBBBBB',
            email: 'BBBBBB',
            isActive: true,
            phone: 'BBBBBB',
            addressLine1: 'BBBBBB',
            addressLine2: 'BBBBBB',
            city: 'BBBBBB',
            country: 'BBBBBB',
            image: 'BBBBBB',
            userLimit: 1,
            creditScore: 1
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

      it('should delete a ExUser', () => {
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
