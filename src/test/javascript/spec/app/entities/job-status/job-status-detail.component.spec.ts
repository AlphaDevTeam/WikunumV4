import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { WikunumTestModule } from '../../../test.module';
import { JobStatusDetailComponent } from 'app/entities/job-status/job-status-detail.component';
import { JobStatus } from 'app/shared/model/job-status.model';

describe('Component Tests', () => {
  describe('JobStatus Management Detail Component', () => {
    let comp: JobStatusDetailComponent;
    let fixture: ComponentFixture<JobStatusDetailComponent>;
    const route = ({ data: of({ jobStatus: new JobStatus(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [WikunumTestModule],
        declarations: [JobStatusDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(JobStatusDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(JobStatusDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load jobStatus on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.jobStatus).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
