import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IEmployeeAccountBalance } from 'app/shared/model/employee-account-balance.model';

type EntityResponseType = HttpResponse<IEmployeeAccountBalance>;
type EntityArrayResponseType = HttpResponse<IEmployeeAccountBalance[]>;

@Injectable({ providedIn: 'root' })
export class EmployeeAccountBalanceService {
  public resourceUrl = SERVER_API_URL + 'api/employee-account-balances';

  constructor(protected http: HttpClient) {}

  create(employeeAccountBalance: IEmployeeAccountBalance): Observable<EntityResponseType> {
    return this.http.post<IEmployeeAccountBalance>(this.resourceUrl, employeeAccountBalance, { observe: 'response' });
  }

  update(employeeAccountBalance: IEmployeeAccountBalance): Observable<EntityResponseType> {
    return this.http.put<IEmployeeAccountBalance>(this.resourceUrl, employeeAccountBalance, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IEmployeeAccountBalance>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IEmployeeAccountBalance[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
