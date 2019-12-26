import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IConfigurationItems } from 'app/shared/model/configuration-items.model';

@Component({
  selector: 'jhi-configuration-items-detail',
  templateUrl: './configuration-items-detail.component.html'
})
export class ConfigurationItemsDetailComponent implements OnInit {
  configurationItems: IConfigurationItems | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ configurationItems }) => {
      this.configurationItems = configurationItems;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
