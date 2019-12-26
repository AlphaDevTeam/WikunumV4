import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { WikunumTestModule } from '../../../test.module';
import { DocumentHistoryDetailComponent } from 'app/entities/document-history/document-history-detail.component';
import { DocumentHistory } from 'app/shared/model/document-history.model';

describe('Component Tests', () => {
  describe('DocumentHistory Management Detail Component', () => {
    let comp: DocumentHistoryDetailComponent;
    let fixture: ComponentFixture<DocumentHistoryDetailComponent>;
    const route = ({ data: of({ documentHistory: new DocumentHistory(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [WikunumTestModule],
        declarations: [DocumentHistoryDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(DocumentHistoryDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(DocumentHistoryDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load documentHistory on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.documentHistory).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
