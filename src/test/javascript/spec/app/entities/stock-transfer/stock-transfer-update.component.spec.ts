import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { WikunumTestModule } from '../../../test.module';
import { StockTransferUpdateComponent } from 'app/entities/stock-transfer/stock-transfer-update.component';
import { StockTransferService } from 'app/entities/stock-transfer/stock-transfer.service';
import { StockTransfer } from 'app/shared/model/stock-transfer.model';

describe('Component Tests', () => {
  describe('StockTransfer Management Update Component', () => {
    let comp: StockTransferUpdateComponent;
    let fixture: ComponentFixture<StockTransferUpdateComponent>;
    let service: StockTransferService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [WikunumTestModule],
        declarations: [StockTransferUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(StockTransferUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(StockTransferUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(StockTransferService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new StockTransfer(123);
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
        const entity = new StockTransfer();
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
