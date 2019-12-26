import { Moment } from 'moment';
import { IDocumentType } from 'app/shared/model/document-type.model';
import { IExUser } from 'app/shared/model/ex-user.model';
import { IChangeLog } from 'app/shared/model/change-log.model';

export interface IDocumentHistory {
  id?: number;
  historyDescription?: string;
  historyDate?: Moment;
  type?: IDocumentType;
  lastModifiedUser?: IExUser;
  createdUser?: IExUser;
  changeLogs?: IChangeLog[];
}

export class DocumentHistory implements IDocumentHistory {
  constructor(
    public id?: number,
    public historyDescription?: string,
    public historyDate?: Moment,
    public type?: IDocumentType,
    public lastModifiedUser?: IExUser,
    public createdUser?: IExUser,
    public changeLogs?: IChangeLog[]
  ) {}
}
