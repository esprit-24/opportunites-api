import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IRegion } from 'app/entities/region/region.model';
import { RegionService } from 'app/entities/region/service/region.service';
import { DepartementService } from '../service/departement.service';
import { IDepartement } from '../departement.model';
import { DepartementFormService } from './departement-form.service';

import { DepartementUpdateComponent } from './departement-update.component';

describe('Departement Management Update Component', () => {
  let comp: DepartementUpdateComponent;
  let fixture: ComponentFixture<DepartementUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let departementFormService: DepartementFormService;
  let departementService: DepartementService;
  let regionService: RegionService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [DepartementUpdateComponent],
      providers: [
        provideHttpClient(),
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(DepartementUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(DepartementUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    departementFormService = TestBed.inject(DepartementFormService);
    departementService = TestBed.inject(DepartementService);
    regionService = TestBed.inject(RegionService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call Region query and add missing value', () => {
      const departement: IDepartement = { id: 21409 };
      const region: IRegion = { id: 3454 };
      departement.region = region;

      const regionCollection: IRegion[] = [{ id: 3454 }];
      jest.spyOn(regionService, 'query').mockReturnValue(of(new HttpResponse({ body: regionCollection })));
      const additionalRegions = [region];
      const expectedCollection: IRegion[] = [...additionalRegions, ...regionCollection];
      jest.spyOn(regionService, 'addRegionToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ departement });
      comp.ngOnInit();

      expect(regionService.query).toHaveBeenCalled();
      expect(regionService.addRegionToCollectionIfMissing).toHaveBeenCalledWith(
        regionCollection,
        ...additionalRegions.map(expect.objectContaining),
      );
      expect(comp.regionsSharedCollection).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const departement: IDepartement = { id: 21409 };
      const region: IRegion = { id: 3454 };
      departement.region = region;

      activatedRoute.data = of({ departement });
      comp.ngOnInit();

      expect(comp.regionsSharedCollection).toContainEqual(region);
      expect(comp.departement).toEqual(departement);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDepartement>>();
      const departement = { id: 13725 };
      jest.spyOn(departementFormService, 'getDepartement').mockReturnValue(departement);
      jest.spyOn(departementService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ departement });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: departement }));
      saveSubject.complete();

      // THEN
      expect(departementFormService.getDepartement).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(departementService.update).toHaveBeenCalledWith(expect.objectContaining(departement));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDepartement>>();
      const departement = { id: 13725 };
      jest.spyOn(departementFormService, 'getDepartement').mockReturnValue({ id: null });
      jest.spyOn(departementService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ departement: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: departement }));
      saveSubject.complete();

      // THEN
      expect(departementFormService.getDepartement).toHaveBeenCalled();
      expect(departementService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDepartement>>();
      const departement = { id: 13725 };
      jest.spyOn(departementService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ departement });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(departementService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareRegion', () => {
      it('should forward to regionService', () => {
        const entity = { id: 3454 };
        const entity2 = { id: 30405 };
        jest.spyOn(regionService, 'compareRegion');
        comp.compareRegion(entity, entity2);
        expect(regionService.compareRegion).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
