import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IItems } from 'app/shared/model/items.model';

type EntityResponseType = HttpResponse<IItems>;
type EntityArrayResponseType = HttpResponse<IItems[]>;

@Injectable({ providedIn: 'root' })
export class ItemsService {
  public resourceUrl = SERVER_API_URL + 'api/items';

  constructor(protected http: HttpClient) {}

  create(items: IItems): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(items);
    return this.http
      .post<IItems>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(items: IItems): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(items);
    return this.http
      .put<IItems>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IItems>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IItems[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(items: IItems): IItems {
    const copy: IItems = Object.assign({}, items, {
      originalStockDate:
        items.originalStockDate && items.originalStockDate.isValid() ? items.originalStockDate.format(DATE_FORMAT) : undefined,
      modifiedStockDate:
        items.modifiedStockDate && items.modifiedStockDate.isValid() ? items.modifiedStockDate.format(DATE_FORMAT) : undefined
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.originalStockDate = res.body.originalStockDate ? moment(res.body.originalStockDate) : undefined;
      res.body.modifiedStockDate = res.body.modifiedStockDate ? moment(res.body.modifiedStockDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((items: IItems) => {
        items.originalStockDate = items.originalStockDate ? moment(items.originalStockDate) : undefined;
        items.modifiedStockDate = items.modifiedStockDate ? moment(items.modifiedStockDate) : undefined;
      });
    }
    return res;
  }
}
