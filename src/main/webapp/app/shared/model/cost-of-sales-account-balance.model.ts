import { ILocation } from 'app/shared/model/location.model';
import { ITransactionType } from 'app/shared/model/transaction-type.model';

export interface ICostOfSalesAccountBalance {
  id?: number;
  balance?: number;
  location?: ILocation;
  transactionType?: ITransactionType;
}

export class CostOfSalesAccountBalance implements ICostOfSalesAccountBalance {
  constructor(public id?: number, public balance?: number, public location?: ILocation, public transactionType?: ITransactionType) {}
}
