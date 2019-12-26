import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { WikunumTestModule } from '../../../test.module';
import { InvoiceDetailsDetailComponent } from 'app/entities/invoice-details/invoice-details-detail.component';
import { InvoiceDetails } from 'app/shared/model/invoice-details.model';

describe('Component Tests', () => {
  describe('InvoiceDetails Management Detail Component', () => {
    let comp: InvoiceDetailsDetailComponent;
    let fixture: ComponentFixture<InvoiceDetailsDetailComponent>;
    const route = ({ data: of({ invoiceDetails: new InvoiceDetails(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [WikunumTestModule],
        declarations: [InvoiceDetailsDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(InvoiceDetailsDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(InvoiceDetailsDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load invoiceDetails on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.invoiceDetails).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
