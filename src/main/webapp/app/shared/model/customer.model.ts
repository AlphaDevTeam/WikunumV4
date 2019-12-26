import { IDocumentHistory } from 'app/shared/model/document-history.model';
import { ILocation } from 'app/shared/model/location.model';

export interface ICustomer {
  id?: number;
  customerCode?: string;
  customerName?: string;
  customerCreditLimit?: number;
  isActive?: boolean;
  rating?: number;
  phone?: string;
  addressLine1?: string;
  addressLine2?: string;
  city?: string;
  country?: string;
  imageContentType?: string;
  image?: any;
  history?: IDocumentHistory;
  location?: ILocation;
}

export class Customer implements ICustomer {
  constructor(
    public id?: number,
    public customerCode?: string,
    public customerName?: string,
    public customerCreditLimit?: number,
    public isActive?: boolean,
    public rating?: number,
    public phone?: string,
    public addressLine1?: string,
    public addressLine2?: string,
    public city?: string,
    public country?: string,
    public imageContentType?: string,
    public image?: any,
    public history?: IDocumentHistory,
    public location?: ILocation
  ) {
    this.isActive = this.isActive || false;
  }
}
