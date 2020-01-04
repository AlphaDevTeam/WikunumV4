import { IItems } from 'app/shared/model/items.model';
import { IStorageBin } from 'app/shared/model/storage-bin.model';
import { IGoodsReceipt } from 'app/shared/model/goods-receipt.model';

export interface IGoodsReceiptDetails {
  id?: number;
  grnQty?: number;
  revisedItemCost?: number;
  item?: IItems;
  storageBin?: IStorageBin;
  grn?: IGoodsReceipt;
}

export class GoodsReceiptDetails implements IGoodsReceiptDetails {
  constructor(
    public id?: number,
    public grnQty?: number,
    public revisedItemCost?: number,
    public item?: IItems,
    public storageBin?: IStorageBin,
    public grn?: IGoodsReceipt
  ) {}
}
