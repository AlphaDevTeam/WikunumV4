import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IExpenseAccount } from 'app/shared/model/expense-account.model';

type EntityResponseType = HttpResponse<IExpenseAccount>;
type EntityArrayResponseType = HttpResponse<IExpenseAccount[]>;

@Injectable({ providedIn: 'root' })
export class ExpenseAccountService {
  public resourceUrl = SERVER_API_URL + 'api/expense-accounts';

  constructor(protected http: HttpClient) {}

  create(expenseAccount: IExpenseAccount): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(expenseAccount);
    return this.http
      .post<IExpenseAccount>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(expenseAccount: IExpenseAccount): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(expenseAccount);
    return this.http
      .put<IExpenseAccount>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IExpenseAccount>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IExpenseAccount[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(expenseAccount: IExpenseAccount): IExpenseAccount {
    const copy: IExpenseAccount = Object.assign({}, expenseAccount, {
      transactionDate:
        expenseAccount.transactionDate && expenseAccount.transactionDate.isValid()
          ? expenseAccount.transactionDate.format(DATE_FORMAT)
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
      res.body.forEach((expenseAccount: IExpenseAccount) => {
        expenseAccount.transactionDate = expenseAccount.transactionDate ? moment(expenseAccount.transactionDate) : undefined;
      });
    }
    return res;
  }
}
