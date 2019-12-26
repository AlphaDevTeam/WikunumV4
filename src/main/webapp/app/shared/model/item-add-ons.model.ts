import { ILocation } from 'app/shared/model/location.model';
import { IItems } from 'app/shared/model/items.model';

export interface IItemAddOns {
  id?: number;
  addonCode?: string;
  addonName?: string;
  addonDescription?: string;
  isActive?: boolean;
  allowSubstract?: boolean;
  addonPrice?: number;
  substractPrice?: number;
  imageContentType?: string;
  image?: any;
  location?: ILocation;
  items?: IItems[];
}

export class ItemAddOns implements IItemAddOns {
  constructor(
    public id?: number,
    public addonCode?: string,
    public addonName?: string,
    public addonDescription?: string,
    public isActive?: boolean,
    public allowSubstract?: boolean,
    public addonPrice?: number,
    public substractPrice?: number,
    public imageContentType?: string,
    public image?: any,
    public location?: ILocation,
    public items?: IItems[]
  ) {
    this.isActive = this.isActive || false;
    this.allowSubstract = this.allowSubstract || false;
  }
}
