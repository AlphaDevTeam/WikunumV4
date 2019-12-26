import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IChangeLog } from 'app/shared/model/change-log.model';

type EntityResponseType = HttpResponse<IChangeLog>;
type EntityArrayResponseType = HttpResponse<IChangeLog[]>;

@Injectable({ providedIn: 'root' })
export class ChangeLogService {
  public resourceUrl = SERVER_API_URL + 'api/change-logs';

  constructor(protected http: HttpClient) {}

  create(changeLog: IChangeLog): Observable<EntityResponseType> {
    return this.http.post<IChangeLog>(this.resourceUrl, changeLog, { observe: 'response' });
  }

  update(changeLog: IChangeLog): Observable<EntityResponseType> {
    return this.http.put<IChangeLog>(this.resourceUrl, changeLog, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IChangeLog>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IChangeLog[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
