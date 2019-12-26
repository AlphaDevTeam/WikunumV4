import { IItems } from 'app/shared/model/items.model';
import { IInvoice } from 'app/shared/model/invoice.model';

export interface IInvoiceDetails {
  id?: number;
  invQty?: number;
  revisedItemSalesPrice?: number;
  item?: IItems;
  inv?: IInvoice;
}

export class InvoiceDetails implements IInvoiceDetails {
  constructor(
    public id?: number,
    public invQty?: number,
    public revisedItemSalesPrice?: number,
    public item?: IItems,
    public inv?: IInvoice
  ) {}
}
