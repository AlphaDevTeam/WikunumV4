import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IInvoiceDetails } from 'app/shared/model/invoice-details.model';

type EntityResponseType = HttpResponse<IInvoiceDetails>;
type EntityArrayResponseType = HttpResponse<IInvoiceDetails[]>;

@Injectable({ providedIn: 'root' })
export class InvoiceDetailsService {
  public resourceUrl = SERVER_API_URL + 'api/invoice-details';

  constructor(protected http: HttpClient) {}

  create(invoiceDetails: IInvoiceDetails): Observable<EntityResponseType> {
    return this.http.post<IInvoiceDetails>(this.resourceUrl, invoiceDetails, { observe: 'response' });
  }

  update(invoiceDetails: IInvoiceDetails): Observable<EntityResponseType> {
    return this.http.put<IInvoiceDetails>(this.resourceUrl, invoiceDetails, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IInvoiceDetails>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IInvoiceDetails[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
