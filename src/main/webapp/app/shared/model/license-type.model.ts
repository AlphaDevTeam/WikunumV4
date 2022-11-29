export interface ILicenseType {
  id?: number;
  licenseTypeCode?: string;
  licenseTypeName?: string;
  validityDays?: number;
  isActive?: boolean;
}

export class LicenseType implements ILicenseType {
  constructor(
    public id?: number,
    public licenseTypeCode?: string,
    public licenseTypeName?: string,
    public validityDays?: number,
    public isActive?: boolean
  ) {
    this.isActive = this.isActive || false;
  }
}
