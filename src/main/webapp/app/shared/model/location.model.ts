import { IDocumentHistory } from 'app/shared/model/document-history.model';
import { ICompany } from 'app/shared/model/company.model';
import { IConfigurationItems } from 'app/shared/model/configuration-items.model';
import { IExUser } from 'app/shared/model/ex-user.model';

export interface ILocation {
  id?: number;
  locationCode?: string;
  locationName?: string;
  isActive?: boolean;
  history?: IDocumentHistory;
  company?: ICompany;
  configitems?: IConfigurationItems[];
  users?: IExUser[];
}

export class Location implements ILocation {
  constructor(
    public id?: number,
    public locationCode?: string,
    public locationName?: string,
    public isActive?: boolean,
    public history?: IDocumentHistory,
    public company?: ICompany,
    public configitems?: IConfigurationItems[],
    public users?: IExUser[]
  ) {
    this.isActive = this.isActive || false;
  }
}
