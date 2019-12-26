import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import './vendor';
import { WikunumSharedModule } from 'app/shared/shared.module';
import { WikunumCoreModule } from 'app/core/core.module';
import { WikunumAppRoutingModule } from './app-routing.module';
import { WikunumHomeModule } from './home/home.module';
import { WikunumEntityModule } from './entities/entity.module';
// jhipster-needle-angular-add-module-import JHipster will add new module here
import { MainComponent } from './layouts/main/main.component';
import { NavbarComponent } from './layouts/navbar/navbar.component';
import { FooterComponent } from './layouts/footer/footer.component';
import { PageRibbonComponent } from './layouts/profiles/page-ribbon.component';
import { ErrorComponent } from './layouts/error/error.component';

@NgModule({
  imports: [
    BrowserModule,
    WikunumSharedModule,
    WikunumCoreModule,
    WikunumHomeModule,
    // jhipster-needle-angular-add-module JHipster will add new module here
    WikunumEntityModule,
    WikunumAppRoutingModule
  ],
  declarations: [MainComponent, NavbarComponent, ErrorComponent, PageRibbonComponent, FooterComponent],
  bootstrap: [MainComponent]
})
export class WikunumAppModule {}
