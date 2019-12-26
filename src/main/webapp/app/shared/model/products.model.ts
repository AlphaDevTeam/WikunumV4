import { IDocumentHistory } from 'app/shared/model/document-history.model';
import { ILocation } from 'app/shared/model/location.model';

export interface IProducts {
  id?: number;
  productCode?: string;
  productName?: string;
  history?: IDocumentHistory;
  location?: ILocation;
}

export class Products implements IProducts {
  constructor(
    public id?: number,
    public productCode?: string,
    public productName?: string,
    public history?: IDocumentHistory,
    public location?: ILocation
  ) {}
}
