import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IConfigurationItems } from 'app/shared/model/configuration-items.model';

type EntityResponseType = HttpResponse<IConfigurationItems>;
type EntityArrayResponseType = HttpResponse<IConfigurationItems[]>;

@Injectable({ providedIn: 'root' })
export class ConfigurationItemsService {
  public resourceUrl = SERVER_API_URL + 'api/configuration-items';

  constructor(protected http: HttpClient) {}

  create(configurationItems: IConfigurationItems): Observable<EntityResponseType> {
    return this.http.post<IConfigurationItems>(this.resourceUrl, configurationItems, { observe: 'response' });
  }

  update(configurationItems: IConfigurationItems): Observable<EntityResponseType> {
    return this.http.put<IConfigurationItems>(this.resourceUrl, configurationItems, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IConfigurationItems>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IConfigurationItems[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
