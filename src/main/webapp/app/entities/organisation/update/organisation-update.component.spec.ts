import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IVille } from 'app/entities/ville/ville.model';
import { VilleService } from 'app/entities/ville/service/ville.service';
import { OrganisationService } from '../service/organisation.service';
import { IOrganisation } from '../organisation.model';
import { OrganisationFormService } from './organisation-form.service';

import { OrganisationUpdateComponent } from './organisation-update.component';

describe('Organisation Management Update Component', () => {
  let comp: OrganisationUpdateComponent;
  let fixture: ComponentFixture<OrganisationUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let organisationFormService: OrganisationFormService;
  let organisationService: OrganisationService;
  let villeService: VilleService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [OrganisationUpdateComponent],
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
      .overrideTemplate(OrganisationUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(OrganisationUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    organisationFormService = TestBed.inject(OrganisationFormService);
    organisationService = TestBed.inject(OrganisationService);
    villeService = TestBed.inject(VilleService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call Ville query and add missing value', () => {
      const organisation: IOrganisation = { id: 7272 };
      const ville: IVille = { id: 13650 };
      organisation.ville = ville;

      const villeCollection: IVille[] = [{ id: 13650 }];
      jest.spyOn(villeService, 'query').mockReturnValue(of(new HttpResponse({ body: villeCollection })));
      const additionalVilles = [ville];
      const expectedCollection: IVille[] = [...additionalVilles, ...villeCollection];
      jest.spyOn(villeService, 'addVilleToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ organisation });
      comp.ngOnInit();

      expect(villeService.query).toHaveBeenCalled();
      expect(villeService.addVilleToCollectionIfMissing).toHaveBeenCalledWith(
        villeCollection,
        ...additionalVilles.map(expect.objectContaining),
      );
      expect(comp.villesSharedCollection).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const organisation: IOrganisation = { id: 7272 };
      const ville: IVille = { id: 13650 };
      organisation.ville = ville;

      activatedRoute.data = of({ organisation });
      comp.ngOnInit();

      expect(comp.villesSharedCollection).toContainEqual(ville);
      expect(comp.organisation).toEqual(organisation);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IOrganisation>>();
      const organisation = { id: 541 };
      jest.spyOn(organisationFormService, 'getOrganisation').mockReturnValue(organisation);
      jest.spyOn(organisationService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ organisation });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: organisation }));
      saveSubject.complete();

      // THEN
      expect(organisationFormService.getOrganisation).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(organisationService.update).toHaveBeenCalledWith(expect.objectContaining(organisation));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IOrganisation>>();
      const organisation = { id: 541 };
      jest.spyOn(organisationFormService, 'getOrganisation').mockReturnValue({ id: null });
      jest.spyOn(organisationService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ organisation: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: organisation }));
      saveSubject.complete();

      // THEN
      expect(organisationFormService.getOrganisation).toHaveBeenCalled();
      expect(organisationService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IOrganisation>>();
      const organisation = { id: 541 };
      jest.spyOn(organisationService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ organisation });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(organisationService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
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
