import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IDomaine } from 'app/entities/domaine/domaine.model';
import { DomaineService } from 'app/entities/domaine/service/domaine.service';
import { IOrganisation } from 'app/entities/organisation/organisation.model';
import { OrganisationService } from 'app/entities/organisation/service/organisation.service';
import { IVille } from 'app/entities/ville/ville.model';
import { VilleService } from 'app/entities/ville/service/ville.service';
import { IOpportunite } from '../opportunite.model';
import { OpportuniteService } from '../service/opportunite.service';
import { OpportuniteFormService } from './opportunite-form.service';

import { OpportuniteUpdateComponent } from './opportunite-update.component';

describe('Opportunite Management Update Component', () => {
  let comp: OpportuniteUpdateComponent;
  let fixture: ComponentFixture<OpportuniteUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let opportuniteFormService: OpportuniteFormService;
  let opportuniteService: OpportuniteService;
  let domaineService: DomaineService;
  let organisationService: OrganisationService;
  let villeService: VilleService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [OpportuniteUpdateComponent],
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
      .overrideTemplate(OpportuniteUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(OpportuniteUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    opportuniteFormService = TestBed.inject(OpportuniteFormService);
    opportuniteService = TestBed.inject(OpportuniteService);
    domaineService = TestBed.inject(DomaineService);
    organisationService = TestBed.inject(OrganisationService);
    villeService = TestBed.inject(VilleService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call Domaine query and add missing value', () => {
      const opportunite: IOpportunite = { id: 8585 };
      const domaine: IDomaine = { id: 14497 };
      opportunite.domaine = domaine;

      const domaineCollection: IDomaine[] = [{ id: 14497 }];
      jest.spyOn(domaineService, 'query').mockReturnValue(of(new HttpResponse({ body: domaineCollection })));
      const additionalDomaines = [domaine];
      const expectedCollection: IDomaine[] = [...additionalDomaines, ...domaineCollection];
      jest.spyOn(domaineService, 'addDomaineToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ opportunite });
      comp.ngOnInit();

      expect(domaineService.query).toHaveBeenCalled();
      expect(domaineService.addDomaineToCollectionIfMissing).toHaveBeenCalledWith(
        domaineCollection,
        ...additionalDomaines.map(expect.objectContaining),
      );
      expect(comp.domainesSharedCollection).toEqual(expectedCollection);
    });

    it('should call Organisation query and add missing value', () => {
      const opportunite: IOpportunite = { id: 8585 };
      const organisation: IOrganisation = { id: 541 };
      opportunite.organisation = organisation;

      const organisationCollection: IOrganisation[] = [{ id: 541 }];
      jest.spyOn(organisationService, 'query').mockReturnValue(of(new HttpResponse({ body: organisationCollection })));
      const additionalOrganisations = [organisation];
      const expectedCollection: IOrganisation[] = [...additionalOrganisations, ...organisationCollection];
      jest.spyOn(organisationService, 'addOrganisationToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ opportunite });
      comp.ngOnInit();

      expect(organisationService.query).toHaveBeenCalled();
      expect(organisationService.addOrganisationToCollectionIfMissing).toHaveBeenCalledWith(
        organisationCollection,
        ...additionalOrganisations.map(expect.objectContaining),
      );
      expect(comp.organisationsSharedCollection).toEqual(expectedCollection);
    });

    it('should call Ville query and add missing value', () => {
      const opportunite: IOpportunite = { id: 8585 };
      const ville: IVille = { id: 13650 };
      opportunite.ville = ville;

      const villeCollection: IVille[] = [{ id: 13650 }];
      jest.spyOn(villeService, 'query').mockReturnValue(of(new HttpResponse({ body: villeCollection })));
      const additionalVilles = [ville];
      const expectedCollection: IVille[] = [...additionalVilles, ...villeCollection];
      jest.spyOn(villeService, 'addVilleToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ opportunite });
      comp.ngOnInit();

      expect(villeService.query).toHaveBeenCalled();
      expect(villeService.addVilleToCollectionIfMissing).toHaveBeenCalledWith(
        villeCollection,
        ...additionalVilles.map(expect.objectContaining),
      );
      expect(comp.villesSharedCollection).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const opportunite: IOpportunite = { id: 8585 };
      const domaine: IDomaine = { id: 14497 };
      opportunite.domaine = domaine;
      const organisation: IOrganisation = { id: 541 };
      opportunite.organisation = organisation;
      const ville: IVille = { id: 13650 };
      opportunite.ville = ville;

      activatedRoute.data = of({ opportunite });
      comp.ngOnInit();

      expect(comp.domainesSharedCollection).toContainEqual(domaine);
      expect(comp.organisationsSharedCollection).toContainEqual(organisation);
      expect(comp.villesSharedCollection).toContainEqual(ville);
      expect(comp.opportunite).toEqual(opportunite);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IOpportunite>>();
      const opportunite = { id: 2855 };
      jest.spyOn(opportuniteFormService, 'getOpportunite').mockReturnValue(opportunite);
      jest.spyOn(opportuniteService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ opportunite });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: opportunite }));
      saveSubject.complete();

      // THEN
      expect(opportuniteFormService.getOpportunite).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(opportuniteService.update).toHaveBeenCalledWith(expect.objectContaining(opportunite));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IOpportunite>>();
      const opportunite = { id: 2855 };
      jest.spyOn(opportuniteFormService, 'getOpportunite').mockReturnValue({ id: null });
      jest.spyOn(opportuniteService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ opportunite: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: opportunite }));
      saveSubject.complete();

      // THEN
      expect(opportuniteFormService.getOpportunite).toHaveBeenCalled();
      expect(opportuniteService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IOpportunite>>();
      const opportunite = { id: 2855 };
      jest.spyOn(opportuniteService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ opportunite });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(opportuniteService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareDomaine', () => {
      it('should forward to domaineService', () => {
        const entity = { id: 14497 };
        const entity2 = { id: 24380 };
        jest.spyOn(domaineService, 'compareDomaine');
        comp.compareDomaine(entity, entity2);
        expect(domaineService.compareDomaine).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareOrganisation', () => {
      it('should forward to organisationService', () => {
        const entity = { id: 541 };
        const entity2 = { id: 7272 };
        jest.spyOn(organisationService, 'compareOrganisation');
        comp.compareOrganisation(entity, entity2);
        expect(organisationService.compareOrganisation).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareVille', () => {
      it('should forward to villeService', () => {
        const entity = { id: 13650 };
        const entity2 = { id: 2100 };
        jest.spyOn(villeService, 'compareVille');
        comp.compareVille(entity, entity2);
        expect(villeService.compareVille).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
