import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { WikunumTestModule } from '../../../test.module';
import { ConfigurationItemsDetailComponent } from 'app/entities/configuration-items/configuration-items-detail.component';
import { ConfigurationItems } from 'app/shared/model/configuration-items.model';

describe('Component Tests', () => {
  describe('ConfigurationItems Management Detail Component', () => {
    let comp: ConfigurationItemsDetailComponent;
    let fixture: ComponentFixture<ConfigurationItemsDetailComponent>;
    const route = ({ data: of({ configurationItems: new ConfigurationItems(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [WikunumTestModule],
        declarations: [ConfigurationItemsDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(ConfigurationItemsDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ConfigurationItemsDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load configurationItems on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.configurationItems).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
