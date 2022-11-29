import { Component, OnInit, OnDestroy } from '@angular/core';
import { Subscription } from 'rxjs';


import { JhiEventManager } from 'ng-jhipster';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { LoginModalService } from 'app/core/login/login-modal.service';
import { AccountService } from 'app/core/auth/account.service';
import { Account } from 'app/core/user/account.model';

import { IMenuItems } from 'app/shared/model/menu-items.model';
import { MenuItemsService } from 'app/entities/menu-items/menu-items.service';


@Component({
  selector: 'jhi-home',
  templateUrl: './home.component.html',
  styleUrls: ['home.scss']
})
export class HomeComponent implements OnInit, OnDestroy {
  account: Account | null = null;
  authSubscription?: Subscription;
  menuItems?: IMenuItems[];
  eventSubscriber?: Subscription;
  ngbPaginationPage = 1;
  page!: number;

  constructor(
  private accountService: AccountService,
  private loginModalService: LoginModalService,
  private eventManager: JhiEventManager,
  private menuItemsService: MenuItemsService,
  ) {}

  loadMenuItems(): void {
      this.menuItemsService
        .getMenu()
        .subscribe(
               (res: HttpResponse<IMenuItems[]>) => this.onSuccess(res.body, res.headers),
               () => this.onError()
        );
        this.registerChangeInMenuItems();
  }

  registerChangeInMenuItems(): void {
    this.eventSubscriber = this.eventManager.subscribe('menuItemsListModification', () => this.loadMenuItems());
  }

  protected onSuccess(data: IMenuItems[] | null, headers: HttpHeaders): void {
    this.menuItems = data ? data : [];
  }

  ngOnInit(): void {
    this.authSubscription = this.accountService.getAuthenticationState().subscribe(account => (this.account = account));
    this.loadMenuItems();
  }

  isAuthenticated(): boolean {
    return this.accountService.isAuthenticated();
  }

  login(): void {
    this.loginModalService.open();
  }

  ngOnDestroy(): void {
    if (this.authSubscription) {
      this.authSubscription.unsubscribe();
    }
  }

  protected onError(): void {
    this.ngbPaginationPage = this.page;
  }
}
