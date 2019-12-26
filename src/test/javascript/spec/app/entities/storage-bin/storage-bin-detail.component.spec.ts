import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { WikunumTestModule } from '../../../test.module';
import { StorageBinDetailComponent } from 'app/entities/storage-bin/storage-bin-detail.component';
import { StorageBin } from 'app/shared/model/storage-bin.model';

describe('Component Tests', () => {
  describe('StorageBin Management Detail Component', () => {
    let comp: StorageBinDetailComponent;
    let fixture: ComponentFixture<StorageBinDetailComponent>;
    const route = ({ data: of({ storageBin: new StorageBin(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [WikunumTestModule],
        declarations: [StorageBinDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(StorageBinDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(StorageBinDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load storageBin on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.storageBin).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
