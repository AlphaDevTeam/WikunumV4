export interface IDocumentType {
  id?: number;
  documentTypeCode?: string;
  documentType?: string;
  isActive?: boolean;
}

export class DocumentType implements IDocumentType {
  constructor(public id?: number, public documentTypeCode?: string, public documentType?: string, public isActive?: boolean) {
    this.isActive = this.isActive || false;
  }
}
