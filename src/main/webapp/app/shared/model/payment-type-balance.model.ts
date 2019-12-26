import { ILocation } from 'app/shared/model/location.model';
import { ITransactionType } from 'app/shared/model/transaction-type.model';
import { IPaymentTypes } from 'app/shared/model/payment-types.model';

export interface IPaymentTypeBalance {
  id?: number;
  balance?: number;
  location?: ILocation;
  transactionType?: ITransactionType;
  payType?: IPaymentTypes;
}

export class PaymentTypeBalance implements IPaymentTypeBalance {
  constructor(
    public id?: number,
    public balance?: number,
    public location?: ILocation,
    public transactionType?: ITransactionType,
    public payType?: IPaymentTypes
  ) {}
}
