import { IProducts } from 'app/shared/model/products.model';
import { ILocation } from 'app/shared/model/location.model';

export interface IModel {
  id?: number;
  modelCode?: string;
  modelName?: string;
  relatedProduct?: IProducts;
  location?: ILocation;
}

export class Model implements IModel {
  constructor(
    public id?: number,
    public modelCode?: string,
    public modelName?: string,
    public relatedProduct?: IProducts,
    public location?: ILocation
  ) {}
}
