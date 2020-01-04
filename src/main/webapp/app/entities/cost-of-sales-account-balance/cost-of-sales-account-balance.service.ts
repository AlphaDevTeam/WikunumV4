import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { ICostOfSalesAccountBalance } from 'app/shared/model/cost-of-sales-account-balance.model';

type EntityResponseType = HttpResponse<ICostOfSalesAccountBalance>;
type EntityArrayResponseType = HttpResponse<ICostOfSalesAccountBalance[]>;

@Injectable({ providedIn: 'root' })
export class CostOfSalesAccountBalanceService {
  public resourceUrl = SERVER_API_URL + 'api/cost-of-sales-account-balances';

  constructor(protected http: HttpClient) {}

  create(costOfSalesAccountBalance: ICostOfSalesAccountBalance): Observable<EntityResponseType> {
    return this.http.post<ICostOfSalesAccountBalance>(this.resourceUrl, costOfSalesAccountBalance, { observe: 'response' });
  }

  update(costOfSalesAccountBalance: ICostOfSalesAccountBalance): Observable<EntityResponseType> {
    return this.http.put<ICostOfSalesAccountBalance>(this.resourceUrl, costOfSalesAccountBalance, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ICostOfSalesAccountBalance>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICostOfSalesAccountBalance[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
