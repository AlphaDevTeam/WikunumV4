import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IPaymentTypeAccount } from 'app/shared/model/payment-type-account.model';

type EntityResponseType = HttpResponse<IPaymentTypeAccount>;
type EntityArrayResponseType = HttpResponse<IPaymentTypeAccount[]>;

@Injectable({ providedIn: 'root' })
export class PaymentTypeAccountService {
  public resourceUrl = SERVER_API_URL + 'api/payment-type-accounts';

  constructor(protected http: HttpClient) {}

  create(paymentTypeAccount: IPaymentTypeAccount): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(paymentTypeAccount);
    return this.http
      .post<IPaymentTypeAccount>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(paymentTypeAccount: IPaymentTypeAccount): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(paymentTypeAccount);
    return this.http
      .put<IPaymentTypeAccount>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IPaymentTypeAccount>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IPaymentTypeAccount[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(paymentTypeAccount: IPaymentTypeAccount): IPaymentTypeAccount {
    const copy: IPaymentTypeAccount = Object.assign({}, paymentTypeAccount, {
      transactionDate:
        paymentTypeAccount.transactionDate && paymentTypeAccount.transactionDate.isValid()
          ? paymentTypeAccount.transactionDate.format(DATE_FORMAT)
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
      res.body.forEach((paymentTypeAccount: IPaymentTypeAccount) => {
        paymentTypeAccount.transactionDate = paymentTypeAccount.transactionDate ? moment(paymentTypeAccount.transactionDate) : undefined;
      });
    }
    return res;
  }
}
