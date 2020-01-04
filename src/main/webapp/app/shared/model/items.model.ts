import { Moment } from 'moment';
import { IModel } from 'app/shared/model/model.model';
import { IProducts } from 'app/shared/model/products.model';
import { ILocation } from 'app/shared/model/location.model';
import { IUnitOfMeasure } from 'app/shared/model/unit-of-measure.model';
import { ICurrency } from 'app/shared/model/currency.model';
import { IItemAddOns } from 'app/shared/model/item-add-ons.model';

export interface IItems {
  id?: number;
  itemCode?: string;
  itemName?: string;
  itemDescription?: string;
  itemPrice?: number;
  itemBarcode?: string;
  itemSupplierBarcode?: string;
  itemPromotionalPrice?: number;
  itemCost?: number;
  isItemOnSale?: boolean;
  expiryDate?: Moment;
  imageContentType?: string;
  image?: any;
  relatedModel?: IModel;
  relatedProduct?: IProducts;
  location?: ILocation;
  unitOfMeasure?: IUnitOfMeasure;
  currency?: ICurrency;
  addons?: IItemAddOns[];
}

export class Items implements IItems {
  constructor(
    public id?: number,
    public itemCode?: string,
    public itemName?: string,
    public itemDescription?: string,
    public itemPrice?: number,
    public itemBarcode?: string,
    public itemSupplierBarcode?: string,
    public itemPromotionalPrice?: number,
    public itemCost?: number,
    public isItemOnSale?: boolean,
    public expiryDate?: Moment,
    public imageContentType?: string,
    public image?: any,
    public relatedModel?: IModel,
    public relatedProduct?: IProducts,
    public location?: ILocation,
    public unitOfMeasure?: IUnitOfMeasure,
    public currency?: ICurrency,
    public addons?: IItemAddOns[]
  ) {
    this.isItemOnSale = this.isItemOnSale || false;
  }
}
