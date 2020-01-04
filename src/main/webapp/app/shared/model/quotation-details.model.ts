import { IItems } from 'app/shared/model/items.model';
import { IQuotation } from 'app/shared/model/quotation.model';

export interface IQuotationDetails {
  id?: number;
  rate?: number;
  description?: string;
  item?: IItems;
  quote?: IQuotation;
}

export class QuotationDetails implements IQuotationDetails {
  constructor(public id?: number, public rate?: number, public description?: string, public item?: IItems, public quote?: IQuotation) {}
}
