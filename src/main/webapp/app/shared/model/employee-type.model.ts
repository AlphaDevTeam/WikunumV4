export interface IEmployeeType {
  id?: number;
  employeeTypeCode?: string;
  employeeTypeName?: string;
  isActive?: boolean;
}

export class EmployeeType implements IEmployeeType {
  constructor(public id?: number, public employeeTypeCode?: string, public employeeTypeName?: string, public isActive?: boolean) {
    this.isActive = this.isActive || false;
  }
}
