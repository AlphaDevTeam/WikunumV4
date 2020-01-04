import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { WikunumTestModule } from '../../../test.module';
import { EmployeeTypeDetailComponent } from 'app/entities/employee-type/employee-type-detail.component';
import { EmployeeType } from 'app/shared/model/employee-type.model';

describe('Component Tests', () => {
  describe('EmployeeType Management Detail Component', () => {
    let comp: EmployeeTypeDetailComponent;
    let fixture: ComponentFixture<EmployeeTypeDetailComponent>;
    const route = ({ data: of({ employeeType: new EmployeeType(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [WikunumTestModule],
        declarations: [EmployeeTypeDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(EmployeeTypeDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(EmployeeTypeDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load employeeType on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.employeeType).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
