import { Moment } from 'moment';
import { IDocumentHistory } from 'app/shared/model/document-history.model';
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
  itemSerial?: string;
  itemSupplierSerial?: string;
  itemPromotionalPrice?: number;
  itemPromotionalPercentage?: number;
  itemCost?: number;
  originalStockDate?: Moment;
  modifiedStockDate?: Moment;
  imageContentType?: string;
  image?: any;
  history?: IDocumentHistory;
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
    public itemSerial?: string,
    public itemSupplierSerial?: string,
    public itemPromotionalPrice?: number,
    public itemPromotionalPercentage?: number,
    public itemCost?: number,
    public originalStockDate?: Moment,
    public modifiedStockDate?: Moment,
    public imageContentType?: string,
    public image?: any,
    public history?: IDocumentHistory,
    public relatedModel?: IModel,
    public relatedProduct?: IProducts,
    public location?: ILocation,
    public unitOfMeasure?: IUnitOfMeasure,
    public currency?: ICurrency,
    public addons?: IItemAddOns[]
  ) {}
}
