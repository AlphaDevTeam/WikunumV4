import { ILocation } from 'app/shared/model/location.model';

export interface ICustomer {
  id?: number;
  customerCode?: string;
  customerName?: string;
  customerCreditLimit?: number;
  email?: string;
  isActive?: boolean;
  rating?: number;
  phone?: string;
  addressLine1?: string;
  addressLine2?: string;
  city?: string;
  country?: string;
  imageContentType?: string;
  image?: any;
  location?: ILocation;
}

export class Customer implements ICustomer {
  constructor(
    public id?: number,
    public customerCode?: string,
    public customerName?: string,
    public customerCreditLimit?: number,
    public email?: string,
    public isActive?: boolean,
    public rating?: number,
    public phone?: string,
    public addressLine1?: string,
    public addressLine2?: string,
    public city?: string,
    public country?: string,
    public imageContentType?: string,
    public image?: any,
    public location?: ILocation
  ) {
    this.isActive = this.isActive || false;
  }
}
