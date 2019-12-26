import { ILocation } from 'app/shared/model/location.model';
import { IInvoice } from 'app/shared/model/invoice.model';

export interface IPaymentTypes {
  id?: number;
  paymentTypesCode?: string;
  paymentTypes?: string;
  paymentTypesChargePer?: number;
  isActive?: boolean;
  location?: ILocation;
  invicePay?: IInvoice;
}

export class PaymentTypes implements IPaymentTypes {
  constructor(
    public id?: number,
    public paymentTypesCode?: string,
    public paymentTypes?: string,
    public paymentTypesChargePer?: number,
    public isActive?: boolean,
    public location?: ILocation,
    public invicePay?: IInvoice
  ) {
    this.isActive = this.isActive || false;
  }
}
