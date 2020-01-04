import { ILocation } from 'app/shared/model/location.model';
import { ITransactionType } from 'app/shared/model/transaction-type.model';
import { IEmployee } from 'app/shared/model/employee.model';

export interface IEmployeeAccountBalance {
  id?: number;
  balance?: number;
  location?: ILocation;
  transactionType?: ITransactionType;
  employee?: IEmployee;
}

export class EmployeeAccountBalance implements IEmployeeAccountBalance {
  constructor(
    public id?: number,
    public balance?: number,
    public location?: ILocation,
    public transactionType?: ITransactionType,
    public employee?: IEmployee
  ) {}
}
