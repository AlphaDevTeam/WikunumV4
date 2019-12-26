import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { WikunumTestModule } from '../../../test.module';
import { ModelDetailComponent } from 'app/entities/model/model-detail.component';
import { Model } from 'app/shared/model/model.model';

describe('Component Tests', () => {
  describe('Model Management Detail Component', () => {
    let comp: ModelDetailComponent;
    let fixture: ComponentFixture<ModelDetailComponent>;
    const route = ({ data: of({ model: new Model(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [WikunumTestModule],
        declarations: [ModelDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(ModelDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ModelDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load model on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.model).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
