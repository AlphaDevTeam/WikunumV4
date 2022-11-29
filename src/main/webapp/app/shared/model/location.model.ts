import { ICompany } from 'app/shared/model/company.model';
import { IConfigurationItems } from 'app/shared/model/configuration-items.model';
import { IExUser } from 'app/shared/model/ex-user.model';

export interface ILocation {
  id?: number;
  locationCode?: string;
  locationName?: string;
  isActive?: boolean;
  email?: string;
  rating?: number;
  phone?: string;
  addressLine1?: string;
  addressLine2?: string;
  city?: string;
  country?: string;
  imageContentType?: string;
  image?: any;
  vatNumber?: string;
  vatPerc?: number;
  company?: ICompany;
  configitems?: IConfigurationItems[];
  users?: IExUser[];
}

export class Location implements ILocation {
  constructor(
    public id?: number,
    public locationCode?: string,
    public locationName?: string,
    public isActive?: boolean,
    public email?: string,
    public rating?: number,
    public phone?: string,
    public addressLine1?: string,
    public addressLine2?: string,
    public city?: string,
    public country?: string,
    public imageContentType?: string,
    public image?: any,
    public vatNumber?: string,
    public vatPerc?: number,
    public company?: ICompany,
    public configitems?: IConfigurationItems[],
    public users?: IExUser[]
  ) {
    this.isActive = this.isActive || false;
  }
}
