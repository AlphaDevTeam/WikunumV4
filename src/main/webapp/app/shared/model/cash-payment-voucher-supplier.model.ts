import { Moment } from 'moment';
import { IDocumentHistory } from 'app/shared/model/document-history.model';
import { ILocation } from 'app/shared/model/location.model';
import { ITransactionType } from 'app/shared/model/transaction-type.model';
import { ISupplier } from 'app/shared/model/supplier.model';

export interface ICashPaymentVoucherSupplier {
  id?: number;
  transactionNumber?: string;
  transactionDate?: Moment;
  transactionDescription?: string;
  transactionAmount?: number;
  history?: IDocumentHistory;
  location?: ILocation;
  transactionType?: ITransactionType;
  supplier?: ISupplier;
}

export class CashPaymentVoucherSupplier implements ICashPaymentVoucherSupplier {
  constructor(
    public id?: number,
    public transactionNumber?: string,
    public transactionDate?: Moment,
    public transactionDescription?: string,
    public transactionAmount?: number,
    public history?: IDocumentHistory,
    public location?: ILocation,
    public transactionType?: ITransactionType,
    public supplier?: ISupplier
  ) {}
}
