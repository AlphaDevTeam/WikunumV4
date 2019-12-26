import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { WikunumTestModule } from '../../../test.module';
import { ChangeLogDetailComponent } from 'app/entities/change-log/change-log-detail.component';
import { ChangeLog } from 'app/shared/model/change-log.model';

describe('Component Tests', () => {
  describe('ChangeLog Management Detail Component', () => {
    let comp: ChangeLogDetailComponent;
    let fixture: ComponentFixture<ChangeLogDetailComponent>;
    const route = ({ data: of({ changeLog: new ChangeLog(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [WikunumTestModule],
        declarations: [ChangeLogDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(ChangeLogDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ChangeLogDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load changeLog on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.changeLog).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
