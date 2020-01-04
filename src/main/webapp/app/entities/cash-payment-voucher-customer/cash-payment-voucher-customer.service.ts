import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { ICashPaymentVoucherCustomer } from 'app/shared/model/cash-payment-voucher-customer.model';

type EntityResponseType = HttpResponse<ICashPaymentVoucherCustomer>;
type EntityArrayResponseType = HttpResponse<ICashPaymentVoucherCustomer[]>;

@Injectable({ providedIn: 'root' })
export class CashPaymentVoucherCustomerService {
  public resourceUrl = SERVER_API_URL + 'api/cash-payment-voucher-customers';

  constructor(protected http: HttpClient) {}

  create(cashPaymentVoucherCustomer: ICashPaymentVoucherCustomer): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(cashPaymentVoucherCustomer);
    return this.http
      .post<ICashPaymentVoucherCustomer>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(cashPaymentVoucherCustomer: ICashPaymentVoucherCustomer): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(cashPaymentVoucherCustomer);
    return this.http
      .put<ICashPaymentVoucherCustomer>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ICashPaymentVoucherCustomer>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ICashPaymentVoucherCustomer[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(cashPaymentVoucherCustomer: ICashPaymentVoucherCustomer): ICashPaymentVoucherCustomer {
    const copy: ICashPaymentVoucherCustomer = Object.assign({}, cashPaymentVoucherCustomer, {
      transactionDate:
        cashPaymentVoucherCustomer.transactionDate && cashPaymentVoucherCustomer.transactionDate.isValid()
          ? cashPaymentVoucherCustomer.transactionDate.format(DATE_FORMAT)
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
      res.body.forEach((cashPaymentVoucherCustomer: ICashPaymentVoucherCustomer) => {
        cashPaymentVoucherCustomer.transactionDate = cashPaymentVoucherCustomer.transactionDate
          ? moment(cashPaymentVoucherCustomer.transactionDate)
          : undefined;
      });
    }
    return res;
  }
}
