import { ILocation } from 'app/shared/model/location.model';

export interface IProducts {
  id?: number;
  productCode?: string;
  productName?: string;
  location?: ILocation;
}

export class Products implements IProducts {
  constructor(public id?: number, public productCode?: string, public productName?: string, public location?: ILocation) {}
}
