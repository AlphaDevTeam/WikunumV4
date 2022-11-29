export interface IStorageBin {
  id?: number;
  binNumber?: string;
  binDescription?: string;
}

export class StorageBin implements IStorageBin {
  constructor(public id?: number, public binNumber?: string, public binDescription?: string) {}
}
