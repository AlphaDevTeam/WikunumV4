import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { ICashReceiptVoucherCustomer } from 'app/shared/model/cash-receipt-voucher-customer.model';

type EntityResponseType = HttpResponse<ICashReceiptVoucherCustomer>;
type EntityArrayResponseType = HttpResponse<ICashReceiptVoucherCustomer[]>;

@Injectable({ providedIn: 'root' })
export class CashReceiptVoucherCustomerService {
  public resourceUrl = SERVER_API_URL + 'api/cash-receipt-voucher-customers';

  constructor(protected http: HttpClient) {}

  create(cashReceiptVoucherCustomer: ICashReceiptVoucherCustomer): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(cashReceiptVoucherCustomer);
    return this.http
      .post<ICashReceiptVoucherCustomer>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(cashReceiptVoucherCustomer: ICashReceiptVoucherCustomer): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(cashReceiptVoucherCustomer);
    return this.http
      .put<ICashReceiptVoucherCustomer>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ICashReceiptVoucherCustomer>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ICashReceiptVoucherCustomer[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(cashReceiptVoucherCustomer: ICashReceiptVoucherCustomer): ICashReceiptVoucherCustomer {
    const copy: ICashReceiptVoucherCustomer = Object.assign({}, cashReceiptVoucherCustomer, {
      transactionDate:
        cashReceiptVoucherCustomer.transactionDate && cashReceiptVoucherCustomer.transactionDate.isValid()
          ? cashReceiptVoucherCustomer.transactionDate.format(DATE_FORMAT)
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
      res.body.forEach((cashReceiptVoucherCustomer: ICashReceiptVoucherCustomer) => {
        cashReceiptVoucherCustomer.transactionDate = cashReceiptVoucherCustomer.transactionDate
          ? moment(cashReceiptVoucherCustomer.transactionDate)
          : undefined;
      });
    }
    return res;
  }
}
