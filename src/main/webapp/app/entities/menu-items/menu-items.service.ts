import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IMenuItems } from 'app/shared/model/menu-items.model';

type EntityResponseType = HttpResponse<IMenuItems>;
type EntityArrayResponseType = HttpResponse<IMenuItems[]>;

@Injectable({ providedIn: 'root' })
export class MenuItemsService {
  public resourceUrl = SERVER_API_URL + 'api/menu-items';

  constructor(protected http: HttpClient) {}

  create(menuItems: IMenuItems): Observable<EntityResponseType> {
    return this.http.post<IMenuItems>(this.resourceUrl, menuItems, { observe: 'response' });
  }

  update(menuItems: IMenuItems): Observable<EntityResponseType> {
    return this.http.put<IMenuItems>(this.resourceUrl, menuItems, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IMenuItems>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IMenuItems[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
