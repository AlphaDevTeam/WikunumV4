import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { ICashReceiptVoucherSupplier } from 'app/shared/model/cash-receipt-voucher-supplier.model';

type EntityResponseType = HttpResponse<ICashReceiptVoucherSupplier>;
type EntityArrayResponseType = HttpResponse<ICashReceiptVoucherSupplier[]>;

@Injectable({ providedIn: 'root' })
export class CashReceiptVoucherSupplierService {
  public resourceUrl = SERVER_API_URL + 'api/cash-receipt-voucher-suppliers';

  constructor(protected http: HttpClient) {}

  create(cashReceiptVoucherSupplier: ICashReceiptVoucherSupplier): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(cashReceiptVoucherSupplier);
    return this.http
      .post<ICashReceiptVoucherSupplier>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(cashReceiptVoucherSupplier: ICashReceiptVoucherSupplier): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(cashReceiptVoucherSupplier);
    return this.http
      .put<ICashReceiptVoucherSupplier>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ICashReceiptVoucherSupplier>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ICashReceiptVoucherSupplier[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(cashReceiptVoucherSupplier: ICashReceiptVoucherSupplier): ICashReceiptVoucherSupplier {
    const copy: ICashReceiptVoucherSupplier = Object.assign({}, cashReceiptVoucherSupplier, {
      transactionDate:
        cashReceiptVoucherSupplier.transactionDate && cashReceiptVoucherSupplier.transactionDate.isValid()
          ? cashReceiptVoucherSupplier.transactionDate.format(DATE_FORMAT)
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
      res.body.forEach((cashReceiptVoucherSupplier: ICashReceiptVoucherSupplier) => {
        cashReceiptVoucherSupplier.transactionDate = cashReceiptVoucherSupplier.transactionDate
          ? moment(cashReceiptVoucherSupplier.transactionDate)
          : undefined;
      });
    }
    return res;
  }
}
