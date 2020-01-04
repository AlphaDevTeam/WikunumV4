import { Moment } from 'moment';
import { IGoodsReceiptDetails } from 'app/shared/model/goods-receipt-details.model';
import { IPurchaseOrder } from 'app/shared/model/purchase-order.model';
import { ISupplier } from 'app/shared/model/supplier.model';
import { ILocation } from 'app/shared/model/location.model';
import { ITransactionType } from 'app/shared/model/transaction-type.model';

export interface IGoodsReceipt {
  id?: number;
  grnNumber?: string;
  grnDate?: Moment;
  poNumber?: string;
  grnAmount?: number;
  cashAmount?: number;
  cardAmount?: number;
  dueAmount?: number;
  details?: IGoodsReceiptDetails[];
  linkedPOs?: IPurchaseOrder[];
  supplier?: ISupplier;
  location?: ILocation;
  transactionType?: ITransactionType;
}

export class GoodsReceipt implements IGoodsReceipt {
  constructor(
    public id?: number,
    public grnNumber?: string,
    public grnDate?: Moment,
    public poNumber?: string,
    public grnAmount?: number,
    public cashAmount?: number,
    public cardAmount?: number,
    public dueAmount?: number,
    public details?: IGoodsReceiptDetails[],
    public linkedPOs?: IPurchaseOrder[],
    public supplier?: ISupplier,
    public location?: ILocation,
    public transactionType?: ITransactionType
  ) {}
}
