import { Moment } from 'moment';
import { IDocumentHistory } from 'app/shared/model/document-history.model';
import { ILocation } from 'app/shared/model/location.model';
import { ITransactionType } from 'app/shared/model/transaction-type.model';
import { ISupplier } from 'app/shared/model/supplier.model';

export interface ISupplierAccount {
  id?: number;
  transactionDate?: Moment;
  transactionDescription?: string;
  transactionAmountDR?: number;
  transactionAmountCR?: number;
  transactionBalance?: number;
  history?: IDocumentHistory;
  location?: ILocation;
  transactionType?: ITransactionType;
  supplier?: ISupplier;
}

export class SupplierAccount implements ISupplierAccount {
  constructor(
    public id?: number,
    public transactionDate?: Moment,
    public transactionDescription?: string,
    public transactionAmountDR?: number,
    public transactionAmountCR?: number,
    public transactionBalance?: number,
    public history?: IDocumentHistory,
    public location?: ILocation,
    public transactionType?: ITransactionType,
    public supplier?: ISupplier
  ) {}
}