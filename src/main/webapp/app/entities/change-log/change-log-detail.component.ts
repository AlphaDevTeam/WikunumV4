import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IChangeLog } from 'app/shared/model/change-log.model';

@Component({
  selector: 'jhi-change-log-detail',
  templateUrl: './change-log-detail.component.html'
})
export class ChangeLogDetailComponent implements OnInit {
  changeLog: IChangeLog | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ changeLog }) => {
      this.changeLog = changeLog;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
