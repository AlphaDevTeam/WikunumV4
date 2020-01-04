import { ICustomer } from 'app/shared/model/customer.model';
import { ILocation } from 'app/shared/model/location.model';
import { ITransactionType } from 'app/shared/model/transaction-type.model';

export interface ICustomerAccountBalance {
  id?: number;
  balance?: number;
  customer?: ICustomer;
  location?: ILocation;
  transactionType?: ITransactionType;
}

export class CustomerAccountBalance implements ICustomerAccountBalance {
  constructor(
    public id?: number,
    public balance?: number,
    public customer?: ICustomer,
    public location?: ILocation,
    public transactionType?: ITransactionType
  ) {}
}
