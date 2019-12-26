import { Moment } from 'moment';
import { IDocumentHistory } from 'app/shared/model/document-history.model';
import { IInvoiceDetails } from 'app/shared/model/invoice-details.model';
import { ICustomer } from 'app/shared/model/customer.model';
import { ITransactionType } from 'app/shared/model/transaction-type.model';
import { ILocation } from 'app/shared/model/location.model';
import { IPaymentTypes } from 'app/shared/model/payment-types.model';

export interface IInvoice {
  id?: number;
  invNumber?: string;
  invDate?: Moment;
  invAmount?: number;
  history?: IDocumentHistory;
  details?: IInvoiceDetails[];
  customer?: ICustomer;
  transactionType?: ITransactionType;
  location?: ILocation;
  payType?: IPaymentTypes;
}

export class Invoice implements IInvoice {
  constructor(
    public id?: number,
    public invNumber?: string,
    public invDate?: Moment,
    public invAmount?: number,
    public history?: IDocumentHistory,
    public details?: IInvoiceDetails[],
    public customer?: ICustomer,
    public transactionType?: ITransactionType,
    public location?: ILocation,
    public payType?: IPaymentTypes
  ) {}
}
