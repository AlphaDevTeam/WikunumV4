import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { ICostOfSalesAccount } from 'app/shared/model/cost-of-sales-account.model';

type EntityResponseType = HttpResponse<ICostOfSalesAccount>;
type EntityArrayResponseType = HttpResponse<ICostOfSalesAccount[]>;

@Injectable({ providedIn: 'root' })
export class CostOfSalesAccountService {
  public resourceUrl = SERVER_API_URL + 'api/cost-of-sales-accounts';

  constructor(protected http: HttpClient) {}

  create(costOfSalesAccount: ICostOfSalesAccount): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(costOfSalesAccount);
    return this.http
      .post<ICostOfSalesAccount>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(costOfSalesAccount: ICostOfSalesAccount): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(costOfSalesAccount);
    return this.http
      .put<ICostOfSalesAccount>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ICostOfSalesAccount>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ICostOfSalesAccount[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(costOfSalesAccount: ICostOfSalesAccount): ICostOfSalesAccount {
    const copy: ICostOfSalesAccount = Object.assign({}, costOfSalesAccount, {
      transactionDate:
        costOfSalesAccount.transactionDate && costOfSalesAccount.transactionDate.isValid()
          ? costOfSalesAccount.transactionDate.format(DATE_FORMAT)
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
      res.body.forEach((costOfSalesAccount: ICostOfSalesAccount) => {
        costOfSalesAccount.transactionDate = costOfSalesAccount.transactionDate ? moment(costOfSalesAccount.transactionDate) : undefined;
      });
    }
    return res;
  }
}
