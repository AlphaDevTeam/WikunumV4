import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { take, map } from 'rxjs/operators';
import { ItemAddOnsService } from 'app/entities/item-add-ons/item-add-ons.service';
import { IItemAddOns, ItemAddOns } from 'app/shared/model/item-add-ons.model';

describe('Service Tests', () => {
  describe('ItemAddOns Service', () => {
    let injector: TestBed;
    let service: ItemAddOnsService;
    let httpMock: HttpTestingController;
    let elemDefault: IItemAddOns;
    let expectedResult: IItemAddOns | IItemAddOns[] | boolean | null;
    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule]
      });
      expectedResult = null;
      injector = getTestBed();
      service = injector.get(ItemAddOnsService);
      httpMock = injector.get(HttpTestingController);

      elemDefault = new ItemAddOns(0, 'AAAAAAA', 'AAAAAAA', 'AAAAAAA', false, false, 0, 0, 'image/png', 'AAAAAAA');
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

      it('should create a ItemAddOns', () => {
        const returnedFromService = Object.assign(
          {
            id: 0
          },
          elemDefault
        );
        const expected = Object.assign({}, returnedFromService);
        service
          .create(new ItemAddOns())
          .pipe(take(1))
          .subscribe(resp => (expectedResult = resp.body));
        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a ItemAddOns', () => {
        const returnedFromService = Object.assign(
          {
            addonCode: 'BBBBBB',
            addonName: 'BBBBBB',
            addonDescription: 'BBBBBB',
            isActive: true,
            allowSubstract: true,
            addonPrice: 1,
            substractPrice: 1,
            image: 'BBBBBB'
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

      it('should return a list of ItemAddOns', () => {
        const returnedFromService = Object.assign(
          {
            addonCode: 'BBBBBB',
            addonName: 'BBBBBB',
            addonDescription: 'BBBBBB',
            isActive: true,
            allowSubstract: true,
            addonPrice: 1,
            substractPrice: 1,
            image: 'BBBBBB'
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

      it('should delete a ItemAddOns', () => {
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
