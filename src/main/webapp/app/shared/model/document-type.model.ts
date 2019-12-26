import { IDocumentHistory } from 'app/shared/model/document-history.model';

export interface IDocumentType {
  id?: number;
  documentTypeCode?: string;
  documentType?: string;
  history?: IDocumentHistory;
}

export class DocumentType implements IDocumentType {
  constructor(public id?: number, public documentTypeCode?: string, public documentType?: string, public history?: IDocumentHistory) {}
}
