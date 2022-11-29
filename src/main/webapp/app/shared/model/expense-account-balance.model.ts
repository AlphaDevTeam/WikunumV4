import { ILocation } from 'app/shared/model/location.model';
import { ITransactionType } from 'app/shared/model/transaction-type.model';
import { IExpense } from 'app/shared/model/expense.model';

export interface IExpenseAccountBalance {
  id?: number;
  balance?: number;
  location?: ILocation;
  transactionType?: ITransactionType;
  expense?: IExpense;
}

export class ExpenseAccountBalance implements IExpenseAccountBalance {
  constructor(
    public id?: number,
    public balance?: number,
    public location?: ILocation,
    public transactionType?: ITransactionType,
    public expense?: IExpense
  ) {}
}
