import { ILocation } from 'app/shared/model/location.model';
import { ITransactionType } from 'app/shared/model/transaction-type.model';

export interface ICashBookBalance {
  id?: number;
  balance?: number;
  location?: ILocation;
  transactionType?: ITransactionType;
}

export class CashBookBalance implements ICashBookBalance {
  constructor(public id?: number, public balance?: number, public location?: ILocation, public transactionType?: ITransactionType) {}
}
