export interface ICurrency {
  id?: number;
  currencyCode?: string;
  currencyName?: string;
  isActive?: boolean;
}

export class Currency implements ICurrency {
  constructor(public id?: number, public currencyCode?: string, public currencyName?: string, public isActive?: boolean) {
    this.isActive = this.isActive || false;
  }
}
