import { Moment } from 'moment';
import { IJobStatus } from 'app/shared/model/job-status.model';
import { IDocumentHistory } from 'app/shared/model/document-history.model';
import { IJobDetails } from 'app/shared/model/job-details.model';
import { ILocation } from 'app/shared/model/location.model';
import { ICustomer } from 'app/shared/model/customer.model';
import { IWorker } from 'app/shared/model/worker.model';

export interface IJob {
  id?: number;
  jobCode?: string;
  jobDescription?: string;
  jobStartDate?: Moment;
  jobEndDate?: Moment;
  jobAmount?: number;
  status?: IJobStatus;
  history?: IDocumentHistory;
  details?: IJobDetails[];
  location?: ILocation;
  customer?: ICustomer;
  assignedTos?: IWorker[];
}

export class Job implements IJob {
  constructor(
    public id?: number,
    public jobCode?: string,
    public jobDescription?: string,
    public jobStartDate?: Moment,
    public jobEndDate?: Moment,
    public jobAmount?: number,
    public status?: IJobStatus,
    public history?: IDocumentHistory,
    public details?: IJobDetails[],
    public location?: ILocation,
    public customer?: ICustomer,
    public assignedTos?: IWorker[]
  ) {}
}
