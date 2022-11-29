import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { WikunumTestModule } from '../../../test.module';
import { CashBookDetailComponent } from 'app/entities/cash-book/cash-book-detail.component';
import { CashBook } from 'app/shared/model/cash-book.model';

describe('Component Tests', () => {
  describe('CashBook Management Detail Component', () => {
    let comp: CashBookDetailComponent;
    let fixture: ComponentFixture<CashBookDetailComponent>;
    const route = ({ data: of({ cashBook: new CashBook(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [WikunumTestModule],
        declarations: [CashBookDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(CashBookDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(CashBookDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load cashBook on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.cashBook).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
