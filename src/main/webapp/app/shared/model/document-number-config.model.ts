import { IDocumentHistory } from 'app/shared/model/document-history.model';
import { IDocumentType } from 'app/shared/model/document-type.model';
import { ILocation } from 'app/shared/model/location.model';
import { ITransactionType } from 'app/shared/model/transaction-type.model';

export interface IDocumentNumberConfig {
  id?: number;
  documentPrefix?: string;
  documentPostfix?: string;
  currentNumber?: number;
  history?: IDocumentHistory;
  document?: IDocumentType;
  location?: ILocation;
  transactionType?: ITransactionType;
}

export class DocumentNumberConfig implements IDocumentNumberConfig {
  constructor(
    public id?: number,
    public documentPrefix?: string,
    public documentPostfix?: string,
    public currentNumber?: number,
    public history?: IDocumentHistory,
    public document?: IDocumentType,
    public location?: ILocation,
    public transactionType?: ITransactionType
  ) {}
}
