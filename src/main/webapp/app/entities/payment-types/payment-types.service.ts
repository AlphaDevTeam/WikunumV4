import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IPaymentTypes } from 'app/shared/model/payment-types.model';

type EntityResponseType = HttpResponse<IPaymentTypes>;
type EntityArrayResponseType = HttpResponse<IPaymentTypes[]>;

@Injectable({ providedIn: 'root' })
export class PaymentTypesService {
  public resourceUrl = SERVER_API_URL + 'api/payment-types';

  constructor(protected http: HttpClient) {}

  create(paymentTypes: IPaymentTypes): Observable<EntityResponseType> {
    return this.http.post<IPaymentTypes>(this.resourceUrl, paymentTypes, { observe: 'response' });
  }

  update(paymentTypes: IPaymentTypes): Observable<EntityResponseType> {
    return this.http.put<IPaymentTypes>(this.resourceUrl, paymentTypes, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IPaymentTypes>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPaymentTypes[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
