import { ILocation } from 'app/shared/model/location.model';

export interface IExpense {
  id?: number;
  expenseCode?: string;
  expenseName?: string;
  expenseLimit?: number;
  isActive?: boolean;
  location?: ILocation;
}

export class Expense implements IExpense {
  constructor(
    public id?: number,
    public expenseCode?: string,
    public expenseName?: string,
    public expenseLimit?: number,
    public isActive?: boolean,
    public location?: ILocation
  ) {
    this.isActive = this.isActive || false;
  }
}
