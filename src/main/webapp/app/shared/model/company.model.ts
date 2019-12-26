import { IDocumentHistory } from 'app/shared/model/document-history.model';

export interface ICompany {
  id?: number;
  companyCode?: string;
  companyName?: string;
  companyAddress?: string;
  companyRegNumber?: string;
  rating?: number;
  phone?: string;
  addressLine1?: string;
  addressLine2?: string;
  city?: string;
  country?: string;
  imageContentType?: string;
  image?: any;
  history?: IDocumentHistory;
}

export class Company implements ICompany {
  constructor(
    public id?: number,
    public companyCode?: string,
    public companyName?: string,
    public companyAddress?: string,
    public companyRegNumber?: string,
    public rating?: number,
    public phone?: string,
    public addressLine1?: string,
    public addressLine2?: string,
    public city?: string,
    public country?: string,
    public imageContentType?: string,
    public image?: any,
    public history?: IDocumentHistory
  ) {}
}
