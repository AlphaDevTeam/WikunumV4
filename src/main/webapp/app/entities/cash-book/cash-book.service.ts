import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { ICashBook } from 'app/shared/model/cash-book.model';

type EntityResponseType = HttpResponse<ICashBook>;
type EntityArrayResponseType = HttpResponse<ICashBook[]>;

@Injectable({ providedIn: 'root' })
export class CashBookService {
  public resourceUrl = SERVER_API_URL + 'api/cash-books';

  constructor(protected http: HttpClient) {}

  create(cashBook: ICashBook): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(cashBook);
    return this.http
      .post<ICashBook>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(cashBook: ICashBook): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(cashBook);
    return this.http
      .put<ICashBook>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ICashBook>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ICashBook[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(cashBook: ICashBook): ICashBook {
    const copy: ICashBook = Object.assign({}, cashBook, {
      transactionDate:
        cashBook.transactionDate && cashBook.transactionDate.isValid() ? cashBook.transactionDate.format(DATE_FORMAT) : undefined
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.transactionDate = res.body.transactionDate ? moment(res.body.transactionDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((cashBook: ICashBook) => {
        cashBook.transactionDate = cashBook.transactionDate ? moment(cashBook.transactionDate) : undefined;
      });
    }
    return res;
  }
}
