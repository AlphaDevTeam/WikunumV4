import { ILocation } from 'app/shared/model/location.model';

export interface IPaymentTypes {
  id?: number;
  paymentTypesCode?: string;
  paymentTypes?: string;
  paymentTypesChargePer?: number;
  isActive?: boolean;
  location?: ILocation;
}

export class PaymentTypes implements IPaymentTypes {
  constructor(
    public id?: number,
    public paymentTypesCode?: string,
    public paymentTypes?: string,
    public paymentTypesChargePer?: number,
    public isActive?: boolean,
    public location?: ILocation
  ) {
    this.isActive = this.isActive || false;
  }
}
