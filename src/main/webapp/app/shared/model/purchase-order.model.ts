import { Moment } from 'moment';
import { IDocumentHistory } from 'app/shared/model/document-history.model';
import { IPurchaseOrderDetails } from 'app/shared/model/purchase-order-details.model';
import { ISupplier } from 'app/shared/model/supplier.model';
import { ILocation } from 'app/shared/model/location.model';
import { ITransactionType } from 'app/shared/model/transaction-type.model';
import { IGoodsReceipt } from 'app/shared/model/goods-receipt.model';

export interface IPurchaseOrder {
  id?: number;
  poNumber?: string;
  poDate?: Moment;
  poAmount?: number;
  history?: IDocumentHistory;
  details?: IPurchaseOrderDetails[];
  supplier?: ISupplier;
  location?: ILocation;
  transactionType?: ITransactionType;
  relatedGRN?: IGoodsReceipt;
}

export class PurchaseOrder implements IPurchaseOrder {
  constructor(
    public id?: number,
    public poNumber?: string,
    public poDate?: Moment,
    public poAmount?: number,
    public history?: IDocumentHistory,
    public details?: IPurchaseOrderDetails[],
    public supplier?: ISupplier,
    public location?: ILocation,
    public transactionType?: ITransactionType,
    public relatedGRN?: IGoodsReceipt
  ) {}
}
