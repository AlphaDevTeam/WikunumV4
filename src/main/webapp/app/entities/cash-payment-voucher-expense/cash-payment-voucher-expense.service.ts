import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { ICashPaymentVoucherExpense } from 'app/shared/model/cash-payment-voucher-expense.model';

type EntityResponseType = HttpResponse<ICashPaymentVoucherExpense>;
type EntityArrayResponseType = HttpResponse<ICashPaymentVoucherExpense[]>;

@Injectable({ providedIn: 'root' })
export class CashPaymentVoucherExpenseService {
  public resourceUrl = SERVER_API_URL + 'api/cash-payment-voucher-expenses';

  constructor(protected http: HttpClient) {}

  create(cashPaymentVoucherExpense: ICashPaymentVoucherExpense): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(cashPaymentVoucherExpense);
    return this.http
      .post<ICashPaymentVoucherExpense>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(cashPaymentVoucherExpense: ICashPaymentVoucherExpense): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(cashPaymentVoucherExpense);
    return this.http
      .put<ICashPaymentVoucherExpense>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ICashPaymentVoucherExpense>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ICashPaymentVoucherExpense[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(cashPaymentVoucherExpense: ICashPaymentVoucherExpense): ICashPaymentVoucherExpense {
    const copy: ICashPaymentVoucherExpense = Object.assign({}, cashPaymentVoucherExpense, {
      transactionDate:
        cashPaymentVoucherExpense.transactionDate && cashPaymentVoucherExpense.transactionDate.isValid()
          ? cashPaymentVoucherExpense.transactionDate.format(DATE_FORMAT)
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
      res.body.forEach((cashPaymentVoucherExpense: ICashPaymentVoucherExpense) => {
        cashPaymentVoucherExpense.transactionDate = cashPaymentVoucherExpense.transactionDate
          ? moment(cashPaymentVoucherExpense.transactionDate)
          : undefined;
      });
    }
    return res;
  }
}
