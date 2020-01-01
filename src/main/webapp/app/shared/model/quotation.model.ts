import { Moment } from 'moment';
import { IQuotationDetails } from 'app/shared/model/quotation-details.model';
import { ILocation } from 'app/shared/model/location.model';

export interface IQuotation {
  id?: number;
  quotationNumber?: string;
  quotationDate?: Moment;
  quotationexpireDate?: Moment;
  quotationTotalAmount?: number;
  quotationTo?: string;
  quotationFrom?: string;
  projectNumber?: string;
  quotationNote?: string;
  details?: IQuotationDetails[];
  location?: ILocation;
}

export class Quotation implements IQuotation {
  constructor(
    public id?: number,
    public quotationNumber?: string,
    public quotationDate?: Moment,
    public quotationexpireDate?: Moment,
    public quotationTotalAmount?: number,
    public quotationTo?: string,
    public quotationFrom?: string,
    public projectNumber?: string,
    public quotationNote?: string,
    public details?: IQuotationDetails[],
    public location?: ILocation
  ) {}
}
