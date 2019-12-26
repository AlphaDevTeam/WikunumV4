import { IDocumentHistory } from 'app/shared/model/document-history.model';

export interface IUnitOfMeasure {
  id?: number;
  unitOfMeasureCode?: string;
  unitOfMeasureDescription?: string;
  history?: IDocumentHistory;
}

export class UnitOfMeasure implements IUnitOfMeasure {
  constructor(
    public id?: number,
    public unitOfMeasureCode?: string,
    public unitOfMeasureDescription?: string,
    public history?: IDocumentHistory
  ) {}
}
