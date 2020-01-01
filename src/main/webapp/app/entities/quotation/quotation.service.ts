import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IQuotation } from 'app/shared/model/quotation.model';

type EntityResponseType = HttpResponse<IQuotation>;
type EntityArrayResponseType = HttpResponse<IQuotation[]>;

@Injectable({ providedIn: 'root' })
export class QuotationService {
  public resourceUrl = SERVER_API_URL + 'api/quotations';

  constructor(protected http: HttpClient) {}

  create(quotation: IQuotation): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(quotation);
    return this.http
      .post<IQuotation>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(quotation: IQuotation): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(quotation);
    return this.http
      .put<IQuotation>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IQuotation>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IQuotation[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(quotation: IQuotation): IQuotation {
    const copy: IQuotation = Object.assign({}, quotation, {
      quotationDate: quotation.quotationDate && quotation.quotationDate.isValid() ? quotation.quotationDate.format(DATE_FORMAT) : undefined,
      quotationexpireDate:
        quotation.quotationexpireDate && quotation.quotationexpireDate.isValid()
          ? quotation.quotationexpireDate.format(DATE_FORMAT)
          : undefined
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.quotationDate = res.body.quotationDate ? moment(res.body.quotationDate) : undefined;
      res.body.quotationexpireDate = res.body.quotationexpireDate ? moment(res.body.quotationexpireDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((quotation: IQuotation) => {
        quotation.quotationDate = quotation.quotationDate ? moment(quotation.quotationDate) : undefined;
        quotation.quotationexpireDate = quotation.quotationexpireDate ? moment(quotation.quotationexpireDate) : undefined;
      });
    }
    return res;
  }
}
