import { ILocation } from 'app/shared/model/location.model';

export interface IConfigurationItems {
  id?: number;
  configCode?: string;
  configDescription?: string;
  configEnabled?: boolean;
  configParamter?: number;
  locations?: ILocation[];
}

export class ConfigurationItems implements IConfigurationItems {
  constructor(
    public id?: number,
    public configCode?: string,
    public configDescription?: string,
    public configEnabled?: boolean,
    public configParamter?: number,
    public locations?: ILocation[]
  ) {
    this.configEnabled = this.configEnabled || false;
  }
}
