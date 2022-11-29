import { Moment } from 'moment';
import { IInvoiceDetails } from 'app/shared/model/invoice-details.model';
import { ICustomer } from 'app/shared/model/customer.model';
import { ITransactionType } from 'app/shared/model/transaction-type.model';
import { ILocation } from 'app/shared/model/location.model';

export interface IInvoice {
  id?: number;
  invNumber?: string;
  invDate?: Moment;
  invTotalAmount?: number;
  cashAmount?: number;
  cardAmount?: number;
  dueAmount?: number;
  details?: IInvoiceDetails[];
  customer?: ICustomer;
  transactionType?: ITransactionType;
  location?: ILocation;
}

export class Invoice implements IInvoice {
  constructor(
    public id?: number,
    public invNumber?: string,
    public invDate?: Moment,
    public invTotalAmount?: number,
    public cashAmount?: number,
    public cardAmount?: number,
    public dueAmount?: number,
    public details?: IInvoiceDetails[],
    public customer?: ICustomer,
    public transactionType?: ITransactionType,
    public location?: ILocation
  ) {}
}
