import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IItemAddOns } from 'app/shared/model/item-add-ons.model';

type EntityResponseType = HttpResponse<IItemAddOns>;
type EntityArrayResponseType = HttpResponse<IItemAddOns[]>;

@Injectable({ providedIn: 'root' })
export class ItemAddOnsService {
  public resourceUrl = SERVER_API_URL + 'api/item-add-ons';

  constructor(protected http: HttpClient) {}

  create(itemAddOns: IItemAddOns): Observable<EntityResponseType> {
    return this.http.post<IItemAddOns>(this.resourceUrl, itemAddOns, { observe: 'response' });
  }

  update(itemAddOns: IItemAddOns): Observable<EntityResponseType> {
    return this.http.put<IItemAddOns>(this.resourceUrl, itemAddOns, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IItemAddOns>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IItemAddOns[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
