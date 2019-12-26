import { IDocumentHistory } from 'app/shared/model/document-history.model';

export interface ITransactionType {
  id?: number;
  transactionTypeCode?: string;
  transactionType?: string;
  isActive?: boolean;
  history?: IDocumentHistory;
}

export class TransactionType implements ITransactionType {
  constructor(
    public id?: number,
    public transactionTypeCode?: string,
    public transactionType?: string,
    public isActive?: boolean,
    public history?: IDocumentHistory
  ) {
    this.isActive = this.isActive || false;
  }
}
