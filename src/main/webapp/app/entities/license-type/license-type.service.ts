import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { ILicenseType } from 'app/shared/model/license-type.model';

type EntityResponseType = HttpResponse<ILicenseType>;
type EntityArrayResponseType = HttpResponse<ILicenseType[]>;

@Injectable({ providedIn: 'root' })
export class LicenseTypeService {
  public resourceUrl = SERVER_API_URL + 'api/license-types';

  constructor(protected http: HttpClient) {}

  create(licenseType: ILicenseType): Observable<EntityResponseType> {
    return this.http.post<ILicenseType>(this.resourceUrl, licenseType, { observe: 'response' });
  }

  update(licenseType: ILicenseType): Observable<EntityResponseType> {
    return this.http.put<ILicenseType>(this.resourceUrl, licenseType, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ILicenseType>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ILicenseType[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
