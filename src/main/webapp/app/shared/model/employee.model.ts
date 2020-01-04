import { ILocation } from 'app/shared/model/location.model';
import { IEmployeeType } from 'app/shared/model/employee-type.model';

export interface IEmployee {
  id?: number;
  firstName?: string;
  lastName?: string;
  email?: string;
  isActive?: boolean;
  phone?: string;
  addressLine1?: string;
  addressLine2?: string;
  city?: string;
  country?: string;
  imageContentType?: string;
  image?: any;
  salary?: number;
  location?: ILocation;
  empType?: IEmployeeType;
}

export class Employee implements IEmployee {
  constructor(
    public id?: number,
    public firstName?: string,
    public lastName?: string,
    public email?: string,
    public isActive?: boolean,
    public phone?: string,
    public addressLine1?: string,
    public addressLine2?: string,
    public city?: string,
    public country?: string,
    public imageContentType?: string,
    public image?: any,
    public salary?: number,
    public location?: ILocation,
    public empType?: IEmployeeType
  ) {
    this.isActive = this.isActive || false;
  }
}
