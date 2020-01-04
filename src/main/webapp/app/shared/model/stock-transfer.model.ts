import { Moment } from 'moment';
import { IItems } from 'app/shared/model/items.model';
import { ILocation } from 'app/shared/model/location.model';

export interface IStockTransfer {
  id?: number;
  transactionNumber?: string;
  transactionDate?: Moment;
  transactionDescription?: string;
  transactionQty?: number;
  item?: IItems;
  locationFrom?: ILocation;
  locationTo?: ILocation;
}

export class StockTransfer implements IStockTransfer {
  constructor(
    public id?: number,
    public transactionNumber?: string,
    public transactionDate?: Moment,
    public transactionDescription?: string,
    public transactionQty?: number,
    public item?: IItems,
    public locationFrom?: ILocation,
    public locationTo?: ILocation
  ) {}
}
