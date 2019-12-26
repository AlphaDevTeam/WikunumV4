import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IPaymentTypeBalance } from 'app/shared/model/payment-type-balance.model';

type EntityResponseType = HttpResponse<IPaymentTypeBalance>;
type EntityArrayResponseType = HttpResponse<IPaymentTypeBalance[]>;

@Injectable({ providedIn: 'root' })
export class PaymentTypeBalanceService {
  public resourceUrl = SERVER_API_URL + 'api/payment-type-balances';

  constructor(protected http: HttpClient) {}

  create(paymentTypeBalance: IPaymentTypeBalance): Observable<EntityResponseType> {
    return this.http.post<IPaymentTypeBalance>(this.resourceUrl, paymentTypeBalance, { observe: 'response' });
  }

  update(paymentTypeBalance: IPaymentTypeBalance): Observable<EntityResponseType> {
    return this.http.put<IPaymentTypeBalance>(this.resourceUrl, paymentTypeBalance, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IPaymentTypeBalance>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPaymentTypeBalance[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
