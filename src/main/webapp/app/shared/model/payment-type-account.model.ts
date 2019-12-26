import { Moment } from 'moment';
import { ILocation } from 'app/shared/model/location.model';
import { ITransactionType } from 'app/shared/model/transaction-type.model';
import { IPaymentTypes } from 'app/shared/model/payment-types.model';

export interface IPaymentTypeAccount {
  id?: number;
  transactionDate?: Moment;
  transactionDescription?: string;
  transactionAmount?: number;
  transactionBalance?: number;
  location?: ILocation;
  transactionType?: ITransactionType;
  payType?: IPaymentTypes;
}

export class PaymentTypeAccount implements IPaymentTypeAccount {
  constructor(
    public id?: number,
    public transactionDate?: Moment,
    public transactionDescription?: string,
    public transactionAmount?: number,
    public transactionBalance?: number,
    public location?: ILocation,
    public transactionType?: ITransactionType,
    public payType?: IPaymentTypes
  ) {}
}
