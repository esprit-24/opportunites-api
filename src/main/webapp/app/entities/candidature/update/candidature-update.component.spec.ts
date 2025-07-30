import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IOpportunite } from 'app/entities/opportunite/opportunite.model';
import { OpportuniteService } from 'app/entities/opportunite/service/opportunite.service';
import { ICandidat } from 'app/entities/candidat/candidat.model';
import { CandidatService } from 'app/entities/candidat/service/candidat.service';
import { ICandidature } from '../candidature.model';
import { CandidatureService } from '../service/candidature.service';
import { CandidatureFormService } from './candidature-form.service';

import { CandidatureUpdateComponent } from './candidature-update.component';

describe('Candidature Management Update Component', () => {
  let comp: CandidatureUpdateComponent;
  let fixture: ComponentFixture<CandidatureUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let candidatureFormService: CandidatureFormService;
  let candidatureService: CandidatureService;
  let opportuniteService: OpportuniteService;
  let candidatService: CandidatService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [CandidatureUpdateComponent],
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
      .overrideTemplate(CandidatureUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CandidatureUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    candidatureFormService = TestBed.inject(CandidatureFormService);
    candidatureService = TestBed.inject(CandidatureService);
    opportuniteService = TestBed.inject(OpportuniteService);
    candidatService = TestBed.inject(CandidatService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call Opportunite query and add missing value', () => {
      const candidature: ICandidature = { id: 12242 };
      const opportunite: IOpportunite = { id: 2855 };
      candidature.opportunite = opportunite;

      const opportuniteCollection: IOpportunite[] = [{ id: 2855 }];
      jest.spyOn(opportuniteService, 'query').mockReturnValue(of(new HttpResponse({ body: opportuniteCollection })));
      const additionalOpportunites = [opportunite];
      const expectedCollection: IOpportunite[] = [...additionalOpportunites, ...opportuniteCollection];
      jest.spyOn(opportuniteService, 'addOpportuniteToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ candidature });
      comp.ngOnInit();

      expect(opportuniteService.query).toHaveBeenCalled();
      expect(opportuniteService.addOpportuniteToCollectionIfMissing).toHaveBeenCalledWith(
        opportuniteCollection,
        ...additionalOpportunites.map(expect.objectContaining),
      );
      expect(comp.opportunitesSharedCollection).toEqual(expectedCollection);
    });

    it('should call Candidat query and add missing value', () => {
      const candidature: ICandidature = { id: 12242 };
      const candidat: ICandidat = { id: 8289 };
      candidature.candidat = candidat;

      const candidatCollection: ICandidat[] = [{ id: 8289 }];
      jest.spyOn(candidatService, 'query').mockReturnValue(of(new HttpResponse({ body: candidatCollection })));
      const additionalCandidats = [candidat];
      const expectedCollection: ICandidat[] = [...additionalCandidats, ...candidatCollection];
      jest.spyOn(candidatService, 'addCandidatToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ candidature });
      comp.ngOnInit();

      expect(candidatService.query).toHaveBeenCalled();
      expect(candidatService.addCandidatToCollectionIfMissing).toHaveBeenCalledWith(
        candidatCollection,
        ...additionalCandidats.map(expect.objectContaining),
      );
      expect(comp.candidatsSharedCollection).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const candidature: ICandidature = { id: 12242 };
      const opportunite: IOpportunite = { id: 2855 };
      candidature.opportunite = opportunite;
      const candidat: ICandidat = { id: 8289 };
      candidature.candidat = candidat;

      activatedRoute.data = of({ candidature });
      comp.ngOnInit();

      expect(comp.opportunitesSharedCollection).toContainEqual(opportunite);
      expect(comp.candidatsSharedCollection).toContainEqual(candidat);
      expect(comp.candidature).toEqual(candidature);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICandidature>>();
      const candidature = { id: 17844 };
      jest.spyOn(candidatureFormService, 'getCandidature').mockReturnValue(candidature);
      jest.spyOn(candidatureService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ candidature });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: candidature }));
      saveSubject.complete();

      // THEN
      expect(candidatureFormService.getCandidature).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(candidatureService.update).toHaveBeenCalledWith(expect.objectContaining(candidature));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICandidature>>();
      const candidature = { id: 17844 };
      jest.spyOn(candidatureFormService, 'getCandidature').mockReturnValue({ id: null });
      jest.spyOn(candidatureService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ candidature: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: candidature }));
      saveSubject.complete();

      // THEN
      expect(candidatureFormService.getCandidature).toHaveBeenCalled();
      expect(candidatureService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICandidature>>();
      const candidature = { id: 17844 };
      jest.spyOn(candidatureService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ candidature });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(candidatureService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareOpportunite', () => {
      it('should forward to opportuniteService', () => {
        const entity = { id: 2855 };
        const entity2 = { id: 8585 };
        jest.spyOn(opportuniteService, 'compareOpportunite');
        comp.compareOpportunite(entity, entity2);
        expect(opportuniteService.compareOpportunite).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareCandidat', () => {
      it('should forward to candidatService', () => {
        const entity = { id: 8289 };
        const entity2 = { id: 13667 };
        jest.spyOn(candidatService, 'compareCandidat');
        comp.compareCandidat(entity, entity2);
        expect(candidatService.compareCandidat).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
