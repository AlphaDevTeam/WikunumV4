import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { ICashReceiptVoucherExpense } from 'app/shared/model/cash-receipt-voucher-expense.model';

type EntityResponseType = HttpResponse<ICashReceiptVoucherExpense>;
type EntityArrayResponseType = HttpResponse<ICashReceiptVoucherExpense[]>;

@Injectable({ providedIn: 'root' })
export class CashReceiptVoucherExpenseService {
  public resourceUrl = SERVER_API_URL + 'api/cash-receipt-voucher-expenses';

  constructor(protected http: HttpClient) {}

  create(cashReceiptVoucherExpense: ICashReceiptVoucherExpense): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(cashReceiptVoucherExpense);
    return this.http
      .post<ICashReceiptVoucherExpense>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(cashReceiptVoucherExpense: ICashReceiptVoucherExpense): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(cashReceiptVoucherExpense);
    return this.http
      .put<ICashReceiptVoucherExpense>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ICashReceiptVoucherExpense>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ICashReceiptVoucherExpense[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(cashReceiptVoucherExpense: ICashReceiptVoucherExpense): ICashReceiptVoucherExpense {
    const copy: ICashReceiptVoucherExpense = Object.assign({}, cashReceiptVoucherExpense, {
      transactionDate:
        cashReceiptVoucherExpense.transactionDate && cashReceiptVoucherExpense.transactionDate.isValid()
          ? cashReceiptVoucherExpense.transactionDate.format(DATE_FORMAT)
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
      res.body.forEach((cashReceiptVoucherExpense: ICashReceiptVoucherExpense) => {
        cashReceiptVoucherExpense.transactionDate = cashReceiptVoucherExpense.transactionDate
          ? moment(cashReceiptVoucherExpense.transactionDate)
          : undefined;
      });
    }
    return res;
  }
}
