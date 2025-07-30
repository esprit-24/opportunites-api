import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IDepartement } from 'app/entities/departement/departement.model';
import { DepartementService } from 'app/entities/departement/service/departement.service';
import { VilleService } from '../service/ville.service';
import { IVille } from '../ville.model';
import { VilleFormService } from './ville-form.service';

import { VilleUpdateComponent } from './ville-update.component';

describe('Ville Management Update Component', () => {
  let comp: VilleUpdateComponent;
  let fixture: ComponentFixture<VilleUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let villeFormService: VilleFormService;
  let villeService: VilleService;
  let departementService: DepartementService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [VilleUpdateComponent],
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
      .overrideTemplate(VilleUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(VilleUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    villeFormService = TestBed.inject(VilleFormService);
    villeService = TestBed.inject(VilleService);
    departementService = TestBed.inject(DepartementService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call Departement query and add missing value', () => {
      const ville: IVille = { id: 2100 };
      const departement: IDepartement = { id: 13725 };
      ville.departement = departement;

      const departementCollection: IDepartement[] = [{ id: 13725 }];
      jest.spyOn(departementService, 'query').mockReturnValue(of(new HttpResponse({ body: departementCollection })));
      const additionalDepartements = [departement];
      const expectedCollection: IDepartement[] = [...additionalDepartements, ...departementCollection];
      jest.spyOn(departementService, 'addDepartementToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ ville });
      comp.ngOnInit();

      expect(departementService.query).toHaveBeenCalled();
      expect(departementService.addDepartementToCollectionIfMissing).toHaveBeenCalledWith(
        departementCollection,
        ...additionalDepartements.map(expect.objectContaining),
      );
      expect(comp.departementsSharedCollection).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const ville: IVille = { id: 2100 };
      const departement: IDepartement = { id: 13725 };
      ville.departement = departement;

      activatedRoute.data = of({ ville });
      comp.ngOnInit();

      expect(comp.departementsSharedCollection).toContainEqual(departement);
      expect(comp.ville).toEqual(ville);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IVille>>();
      const ville = { id: 13650 };
      jest.spyOn(villeFormService, 'getVille').mockReturnValue(ville);
      jest.spyOn(villeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ ville });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: ville }));
      saveSubject.complete();

      // THEN
      expect(villeFormService.getVille).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(villeService.update).toHaveBeenCalledWith(expect.objectContaining(ville));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IVille>>();
      const ville = { id: 13650 };
      jest.spyOn(villeFormService, 'getVille').mockReturnValue({ id: null });
      jest.spyOn(villeService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ ville: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: ville }));
      saveSubject.complete();

      // THEN
      expect(villeFormService.getVille).toHaveBeenCalled();
      expect(villeService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IVille>>();
      const ville = { id: 13650 };
      jest.spyOn(villeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ ville });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(villeService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareDepartement', () => {
      it('should forward to departementService', () => {
        const entity = { id: 13725 };
        const entity2 = { id: 21409 };
        jest.spyOn(departementService, 'compareDepartement');
        comp.compareDepartement(entity, entity2);
        expect(departementService.compareDepartement).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
