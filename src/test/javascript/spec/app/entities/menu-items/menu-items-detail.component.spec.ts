import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { WikunumTestModule } from '../../../test.module';
import { MenuItemsDetailComponent } from 'app/entities/menu-items/menu-items-detail.component';
import { MenuItems } from 'app/shared/model/menu-items.model';

describe('Component Tests', () => {
  describe('MenuItems Management Detail Component', () => {
    let comp: MenuItemsDetailComponent;
    let fixture: ComponentFixture<MenuItemsDetailComponent>;
    const route = ({ data: of({ menuItems: new MenuItems(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [WikunumTestModule],
        declarations: [MenuItemsDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(MenuItemsDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(MenuItemsDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load menuItems on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.menuItems).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
