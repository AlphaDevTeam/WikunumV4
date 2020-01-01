import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { WikunumTestModule } from '../../../test.module';
import { LicenseTypeDetailComponent } from 'app/entities/license-type/license-type-detail.component';
import { LicenseType } from 'app/shared/model/license-type.model';

describe('Component Tests', () => {
  describe('LicenseType Management Detail Component', () => {
    let comp: LicenseTypeDetailComponent;
    let fixture: ComponentFixture<LicenseTypeDetailComponent>;
    const route = ({ data: of({ licenseType: new LicenseType(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [WikunumTestModule],
        declarations: [LicenseTypeDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(LicenseTypeDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(LicenseTypeDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load licenseType on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.licenseType).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
