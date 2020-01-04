import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { ISalesAccount } from 'app/shared/model/sales-account.model';

type EntityResponseType = HttpResponse<ISalesAccount>;
type EntityArrayResponseType = HttpResponse<ISalesAccount[]>;

@Injectable({ providedIn: 'root' })
export class SalesAccountService {
  public resourceUrl = SERVER_API_URL + 'api/sales-accounts';

  constructor(protected http: HttpClient) {}

  create(salesAccount: ISalesAccount): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(salesAccount);
    return this.http
      .post<ISalesAccount>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(salesAccount: ISalesAccount): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(salesAccount);
    return this.http
      .put<ISalesAccount>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ISalesAccount>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ISalesAccount[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(salesAccount: ISalesAccount): ISalesAccount {
    const copy: ISalesAccount = Object.assign({}, salesAccount, {
      transactionDate:
        salesAccount.transactionDate && salesAccount.transactionDate.isValid()
          ? salesAccount.transactionDate.format(DATE_FORMAT)
          : undefined
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
      res.body.forEach((salesAccount: ISalesAccount) => {
        salesAccount.transactionDate = salesAccount.transactionDate ? moment(salesAccount.transactionDate) : undefined;
      });
    }
    return res;
  }
}
