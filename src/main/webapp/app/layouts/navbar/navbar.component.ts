import { Component, OnInit, OnDestroy } from '@angular/core';
import { Subscription } from 'rxjs';
import { NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiParseLinks } from 'ng-jhipster';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { Router } from '@angular/router';

import { VERSION } from 'app/app.constants';
import { AccountService } from 'app/core/auth/account.service';
import { LoginModalService } from 'app/core/login/login-modal.service';
import { LoginService } from 'app/core/login/login.service';
import { ProfileService } from 'app/layouts/profiles/profile.service';

import { ExUserService } from 'app/entities/ex-user/ex-user.service';
import { IExUser } from 'app/shared/model/ex-user.model';

import { IMenuItems } from 'app/shared/model/menu-items.model';
import { MenuItemsService } from 'app/entities/menu-items/menu-items.service';

@Component({
  selector: 'jhi-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['navbar.scss']
})
export class NavbarComponent implements OnInit, OnDestroy {
  inProduction?: boolean;
  isNavbarCollapsed = true;
  swaggerEnabled?: boolean;
  version: string;
  menuItems?: IMenuItems[];
  eventSubscriber?: Subscription;
  exuser?: IExUser;
  ngbPaginationPage = 1;
  page!: number;

  constructor(
    private loginService: LoginService,
    private accountService: AccountService,
    private menuItemsService: MenuItemsService,
    private exUserService: ExUserService,
    private loginModalService: LoginModalService,
    private profileService: ProfileService,
    private eventManager: JhiEventManager,
    private router: Router
  ) {
    this.version = VERSION ? (VERSION.toLowerCase().startsWith('v') ? VERSION : 'v' + VERSION) : '';
  }

  ngOnInit(): void {

    this.menuItems = [];

    this.profileService.getProfileInfo().subscribe(profileInfo => {
      this.inProduction = profileInfo.inProduction;
      this.swaggerEnabled = profileInfo.swaggerEnabled;
    });

    this.loadMenuItems();


  }

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

  collapseNavbar(): void {
    this.isNavbarCollapsed = true;
  }

  isAuthenticated(): boolean {
    return this.accountService.isAuthenticated();
  }

  login(): void {
    this.loginModalService.open();
  }

  logout(): void {
    this.collapseNavbar();
    this.loginService.logout();
    this.router.navigate(['']);
  }

  toggleNavbar(): void {
    this.isNavbarCollapsed = !this.isNavbarCollapsed;
  }

  getImageUrl(): string {
    return this.isAuthenticated() ? this.accountService.getImageUrl() : '';
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }
  protected onError(): void {
    this.ngbPaginationPage = this.page;
  }
}
