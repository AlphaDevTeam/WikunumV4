import { IDocumentHistory } from 'app/shared/model/document-history.model';

export interface IChangeLog {
  id?: number;
  changeKey?: string;
  changeFrom?: string;
  changeTo?: string;
  documentHistories?: IDocumentHistory[];
}

export class ChangeLog implements IChangeLog {
  constructor(
    public id?: number,
    public changeKey?: string,
    public changeFrom?: string,
    public changeTo?: string,
    public documentHistories?: IDocumentHistory[]
  ) {}
}
