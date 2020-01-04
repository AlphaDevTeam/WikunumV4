import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IUserPermissions } from 'app/shared/model/user-permissions.model';

type EntityResponseType = HttpResponse<IUserPermissions>;
type EntityArrayResponseType = HttpResponse<IUserPermissions[]>;

@Injectable({ providedIn: 'root' })
export class UserPermissionsService {
  public resourceUrl = SERVER_API_URL + 'api/user-permissions';

  constructor(protected http: HttpClient) {}

  create(userPermissions: IUserPermissions): Observable<EntityResponseType> {
    return this.http.post<IUserPermissions>(this.resourceUrl, userPermissions, { observe: 'response' });
  }

  update(userPermissions: IUserPermissions): Observable<EntityResponseType> {
    return this.http.put<IUserPermissions>(this.resourceUrl, userPermissions, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IUserPermissions>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IUserPermissions[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
