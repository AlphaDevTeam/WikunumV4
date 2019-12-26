import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { WikunumTestModule } from '../../../test.module';
import { ItemBinCardDetailComponent } from 'app/entities/item-bin-card/item-bin-card-detail.component';
import { ItemBinCard } from 'app/shared/model/item-bin-card.model';

describe('Component Tests', () => {
  describe('ItemBinCard Management Detail Component', () => {
    let comp: ItemBinCardDetailComponent;
    let fixture: ComponentFixture<ItemBinCardDetailComponent>;
    const route = ({ data: of({ itemBinCard: new ItemBinCard(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [WikunumTestModule],
        declarations: [ItemBinCardDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(ItemBinCardDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ItemBinCardDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load itemBinCard on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.itemBinCard).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
