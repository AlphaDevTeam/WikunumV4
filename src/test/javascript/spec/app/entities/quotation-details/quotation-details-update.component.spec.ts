import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { WikunumTestModule } from '../../../test.module';
import { QuotationDetailsUpdateComponent } from 'app/entities/quotation-details/quotation-details-update.component';
import { QuotationDetailsService } from 'app/entities/quotation-details/quotation-details.service';
import { QuotationDetails } from 'app/shared/model/quotation-details.model';

describe('Component Tests', () => {
  describe('QuotationDetails Management Update Component', () => {
    let comp: QuotationDetailsUpdateComponent;
    let fixture: ComponentFixture<QuotationDetailsUpdateComponent>;
    let service: QuotationDetailsService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [WikunumTestModule],
        declarations: [QuotationDetailsUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(QuotationDetailsUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(QuotationDetailsUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(QuotationDetailsService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new QuotationDetails(123);
        spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.update).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));

      it('Should call create service on save for new entity', fakeAsync(() => {
        // GIVEN
        const entity = new QuotationDetails();
        spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));
    });
  });
});
