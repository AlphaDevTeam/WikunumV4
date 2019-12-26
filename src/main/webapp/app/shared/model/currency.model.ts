import { IDocumentHistory } from 'app/shared/model/document-history.model';

export interface ICurrency {
  id?: number;
  currencyCode?: string;
  currencyName?: string;
  history?: IDocumentHistory;
}

export class Currency implements ICurrency {
  constructor(public id?: number, public currencyCode?: string, public currencyName?: string, public history?: IDocumentHistory) {}
}
