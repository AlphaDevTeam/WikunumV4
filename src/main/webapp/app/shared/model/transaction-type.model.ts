export interface ITransactionType {
  id?: number;
  transactionTypeCode?: string;
  transactionType?: string;
  isActive?: boolean;
}

export class TransactionType implements ITransactionType {
  constructor(public id?: number, public transactionTypeCode?: string, public transactionType?: string, public isActive?: boolean) {
    this.isActive = this.isActive || false;
  }
}
