import { IDocumentHistory } from 'app/shared/model/document-history.model';
import { IMenuItems } from 'app/shared/model/menu-items.model';
import { IExUser } from 'app/shared/model/ex-user.model';
import { IUserGroup } from 'app/shared/model/user-group.model';

export interface IUserPermissions {
  id?: number;
  userPermKey?: string;
  userPermDescription?: string;
  isActive?: boolean;
  history?: IDocumentHistory;
  menuItems?: IMenuItems[];
  users?: IExUser[];
  userGroups?: IUserGroup[];
}

export class UserPermissions implements IUserPermissions {
  constructor(
    public id?: number,
    public userPermKey?: string,
    public userPermDescription?: string,
    public isActive?: boolean,
    public history?: IDocumentHistory,
    public menuItems?: IMenuItems[],
    public users?: IExUser[],
    public userGroups?: IUserGroup[]
  ) {
    this.isActive = this.isActive || false;
  }
}
