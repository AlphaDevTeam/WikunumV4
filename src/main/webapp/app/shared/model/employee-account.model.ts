import { Moment } from 'moment';
import { ILocation } from 'app/shared/model/location.model';
import { ITransactionType } from 'app/shared/model/transaction-type.model';
import { IEmployee } from 'app/shared/model/employee.model';

export interface IEmployeeAccount {
  id?: number;
  transactionDate?: Moment;
  transactionDescription?: string;
  transactionAmountDR?: number;
  transactionAmountCR?: number;
  transactionBalance?: number;
  location?: ILocation;
  transactionType?: ITransactionType;
  employee?: IEmployee;
}

export class EmployeeAccount implements IEmployeeAccount {
  constructor(
    public id?: number,
    public transactionDate?: Moment,
    public transactionDescription?: string,
    public transactionAmountDR?: number,
    public transactionAmountCR?: number,
    public transactionBalance?: number,
    public location?: ILocation,
    public transactionType?: ITransactionType,
    public employee?: IEmployee
  ) {}
}
