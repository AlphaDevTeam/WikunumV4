import { IUserPermissions } from 'app/shared/model/user-permissions.model';

export interface IMenuItems {
  id?: number;
  menuName?: string;
  menuURL?: string;
  isActive?: boolean;
  userPermissions?: IUserPermissions[];
}

export class MenuItems implements IMenuItems {
  constructor(
    public id?: number,
    public menuName?: string,
    public menuURL?: string,
    public isActive?: boolean,
    public userPermissions?: IUserPermissions[]
  ) {
    this.isActive = this.isActive || false;
  }
}
