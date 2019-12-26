import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IExpenseAccountBalance } from 'app/shared/model/expense-account-balance.model';

type EntityResponseType = HttpResponse<IExpenseAccountBalance>;
type EntityArrayResponseType = HttpResponse<IExpenseAccountBalance[]>;

@Injectable({ providedIn: 'root' })
export class ExpenseAccountBalanceService {
  public resourceUrl = SERVER_API_URL + 'api/expense-account-balances';

  constructor(protected http: HttpClient) {}

  create(expenseAccountBalance: IExpenseAccountBalance): Observable<EntityResponseType> {
    return this.http.post<IExpenseAccountBalance>(this.resourceUrl, expenseAccountBalance, { observe: 'response' });
  }

  update(expenseAccountBalance: IExpenseAccountBalance): Observable<EntityResponseType> {
    return this.http.put<IExpenseAccountBalance>(this.resourceUrl, expenseAccountBalance, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IExpenseAccountBalance>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IExpenseAccountBalance[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
