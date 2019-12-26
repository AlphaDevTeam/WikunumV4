import { Moment } from 'moment';
import { IDocumentHistory } from 'app/shared/model/document-history.model';
import { ILocation } from 'app/shared/model/location.model';
import { IItems } from 'app/shared/model/items.model';

export interface IItemBinCard {
  id?: number;
  transactionDate?: Moment;
  transactionDescription?: string;
  transactionQty?: number;
  transactionBalance?: number;
  history?: IDocumentHistory;
  location?: ILocation;
  item?: IItems;
}

export class ItemBinCard implements IItemBinCard {
  constructor(
    public id?: number,
    public transactionDate?: Moment,
    public transactionDescription?: string,
    public transactionQty?: number,
    public transactionBalance?: number,
    public history?: IDocumentHistory,
    public location?: ILocation,
    public item?: IItems
  ) {}
}
