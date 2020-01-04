import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IQuotationDetails } from 'app/shared/model/quotation-details.model';

type EntityResponseType = HttpResponse<IQuotationDetails>;
type EntityArrayResponseType = HttpResponse<IQuotationDetails[]>;

@Injectable({ providedIn: 'root' })
export class QuotationDetailsService {
  public resourceUrl = SERVER_API_URL + 'api/quotation-details';

  constructor(protected http: HttpClient) {}

  create(quotationDetails: IQuotationDetails): Observable<EntityResponseType> {
    return this.http.post<IQuotationDetails>(this.resourceUrl, quotationDetails, { observe: 'response' });
  }

  update(quotationDetails: IQuotationDetails): Observable<EntityResponseType> {
    return this.http.put<IQuotationDetails>(this.resourceUrl, quotationDetails, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IQuotationDetails>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IQuotationDetails[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
