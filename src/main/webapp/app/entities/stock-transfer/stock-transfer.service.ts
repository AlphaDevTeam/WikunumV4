import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IStockTransfer } from 'app/shared/model/stock-transfer.model';

type EntityResponseType = HttpResponse<IStockTransfer>;
type EntityArrayResponseType = HttpResponse<IStockTransfer[]>;

@Injectable({ providedIn: 'root' })
export class StockTransferService {
  public resourceUrl = SERVER_API_URL + 'api/stock-transfers';

  constructor(protected http: HttpClient) {}

  create(stockTransfer: IStockTransfer): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(stockTransfer);
    return this.http
      .post<IStockTransfer>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(stockTransfer: IStockTransfer): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(stockTransfer);
    return this.http
      .put<IStockTransfer>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IStockTransfer>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IStockTransfer[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(stockTransfer: IStockTransfer): IStockTransfer {
    const copy: IStockTransfer = Object.assign({}, stockTransfer, {
      transactionDate:
        stockTransfer.transactionDate && stockTransfer.transactionDate.isValid()
          ? stockTransfer.transactionDate.format(DATE_FORMAT)
          : undefined
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.transactionDate = res.body.transactionDate ? moment(res.body.transactionDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((stockTransfer: IStockTransfer) => {
        stockTransfer.transactionDate = stockTransfer.transactionDate ? moment(stockTransfer.transactionDate) : undefined;
      });
    }
    return res;
  }
}
