export interface IUnitOfMeasure {
  id?: number;
  unitOfMeasureCode?: string;
  unitOfMeasureDescription?: string;
  isActive?: boolean;
}

export class UnitOfMeasure implements IUnitOfMeasure {
  constructor(public id?: number, public unitOfMeasureCode?: string, public unitOfMeasureDescription?: string, public isActive?: boolean) {
    this.isActive = this.isActive || false;
  }
}
