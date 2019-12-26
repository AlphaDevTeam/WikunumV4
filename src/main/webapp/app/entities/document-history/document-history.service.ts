import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IDocumentHistory } from 'app/shared/model/document-history.model';

type EntityResponseType = HttpResponse<IDocumentHistory>;
type EntityArrayResponseType = HttpResponse<IDocumentHistory[]>;

@Injectable({ providedIn: 'root' })
export class DocumentHistoryService {
  public resourceUrl = SERVER_API_URL + 'api/document-histories';

  constructor(protected http: HttpClient) {}

  create(documentHistory: IDocumentHistory): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(documentHistory);
    return this.http
      .post<IDocumentHistory>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(documentHistory: IDocumentHistory): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(documentHistory);
    return this.http
      .put<IDocumentHistory>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IDocumentHistory>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IDocumentHistory[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(documentHistory: IDocumentHistory): IDocumentHistory {
    const copy: IDocumentHistory = Object.assign({}, documentHistory, {
      historyDate: documentHistory.historyDate && documentHistory.historyDate.isValid() ? documentHistory.historyDate.toJSON() : undefined
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.historyDate = res.body.historyDate ? moment(res.body.historyDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((documentHistory: IDocumentHistory) => {
        documentHistory.historyDate = documentHistory.historyDate ? moment(documentHistory.historyDate) : undefined;
      });
    }
    return res;
  }
}
