import { IDocumentHistory } from 'app/shared/model/document-history.model';

export interface IStorageBin {
  id?: number;
  binNumber?: string;
  binDescription?: string;
  history?: IDocumentHistory;
}

export class StorageBin implements IStorageBin {
  constructor(public id?: number, public binNumber?: string, public binDescription?: string, public history?: IDocumentHistory) {}
}
