import { ILocation } from 'app/shared/model/location.model';
import { ITransactionType } from 'app/shared/model/transaction-type.model';

export interface IPurchaseAccountBalance {
  id?: number;
  balance?: number;
  location?: ILocation;
  transactionType?: ITransactionType;
}

export class PurchaseAccountBalance implements IPurchaseAccountBalance {
  constructor(public id?: number, public balance?: number, public location?: ILocation, public transactionType?: ITransactionType) {}
}
