import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IMenuItems } from 'app/shared/model/menu-items.model';

@Component({
  selector: 'jhi-menu-items-detail',
  templateUrl: './menu-items-detail.component.html'
})
export class MenuItemsDetailComponent implements OnInit {
  menuItems: IMenuItems | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ menuItems }) => {
      this.menuItems = menuItems;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
