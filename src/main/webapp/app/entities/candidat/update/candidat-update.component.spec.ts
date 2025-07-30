import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/service/user.service';
import { IProfil } from 'app/entities/profil/profil.model';
import { ProfilService } from 'app/entities/profil/service/profil.service';
import { IDomaine } from 'app/entities/domaine/domaine.model';
import { DomaineService } from 'app/entities/domaine/service/domaine.service';
import { ICandidat } from '../candidat.model';
import { CandidatService } from '../service/candidat.service';
import { CandidatFormService } from './candidat-form.service';

import { CandidatUpdateComponent } from './candidat-update.component';

describe('Candidat Management Update Component', () => {
  let comp: CandidatUpdateComponent;
  let fixture: ComponentFixture<CandidatUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let candidatFormService: CandidatFormService;
  let candidatService: CandidatService;
  let userService: UserService;
  let profilService: ProfilService;
  let domaineService: DomaineService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [CandidatUpdateComponent],
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
      .overrideTemplate(CandidatUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CandidatUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    candidatFormService = TestBed.inject(CandidatFormService);
    candidatService = TestBed.inject(CandidatService);
    userService = TestBed.inject(UserService);
    profilService = TestBed.inject(ProfilService);
    domaineService = TestBed.inject(DomaineService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call User query and add missing value', () => {
      const candidat: ICandidat = { id: 13667 };
      const user: IUser = { id: 3944 };
      candidat.user = user;

      const userCollection: IUser[] = [{ id: 3944 }];
      jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
      const additionalUsers = [user];
      const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
      jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ candidat });
      comp.ngOnInit();

      expect(userService.query).toHaveBeenCalled();
      expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(
        userCollection,
        ...additionalUsers.map(expect.objectContaining),
      );
      expect(comp.usersSharedCollection).toEqual(expectedCollection);
    });

    it('should call Profil query and add missing value', () => {
      const candidat: ICandidat = { id: 13667 };
      const profil: IProfil = { id: 12279 };
      candidat.profil = profil;

      const profilCollection: IProfil[] = [{ id: 12279 }];
      jest.spyOn(profilService, 'query').mockReturnValue(of(new HttpResponse({ body: profilCollection })));
      const additionalProfils = [profil];
      const expectedCollection: IProfil[] = [...additionalProfils, ...profilCollection];
      jest.spyOn(profilService, 'addProfilToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ candidat });
      comp.ngOnInit();

      expect(profilService.query).toHaveBeenCalled();
      expect(profilService.addProfilToCollectionIfMissing).toHaveBeenCalledWith(
        profilCollection,
        ...additionalProfils.map(expect.objectContaining),
      );
      expect(comp.profilsSharedCollection).toEqual(expectedCollection);
    });

    it('should call Domaine query and add missing value', () => {
      const candidat: ICandidat = { id: 13667 };
      const domaine: IDomaine = { id: 14497 };
      candidat.domaine = domaine;

      const domaineCollection: IDomaine[] = [{ id: 14497 }];
      jest.spyOn(domaineService, 'query').mockReturnValue(of(new HttpResponse({ body: domaineCollection })));
      const additionalDomaines = [domaine];
      const expectedCollection: IDomaine[] = [...additionalDomaines, ...domaineCollection];
      jest.spyOn(domaineService, 'addDomaineToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ candidat });
      comp.ngOnInit();

      expect(domaineService.query).toHaveBeenCalled();
      expect(domaineService.addDomaineToCollectionIfMissing).toHaveBeenCalledWith(
        domaineCollection,
        ...additionalDomaines.map(expect.objectContaining),
      );
      expect(comp.domainesSharedCollection).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const candidat: ICandidat = { id: 13667 };
      const user: IUser = { id: 3944 };
      candidat.user = user;
      const profil: IProfil = { id: 12279 };
      candidat.profil = profil;
      const domaine: IDomaine = { id: 14497 };
      candidat.domaine = domaine;

      activatedRoute.data = of({ candidat });
      comp.ngOnInit();

      expect(comp.usersSharedCollection).toContainEqual(user);
      expect(comp.profilsSharedCollection).toContainEqual(profil);
      expect(comp.domainesSharedCollection).toContainEqual(domaine);
      expect(comp.candidat).toEqual(candidat);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICandidat>>();
      const candidat = { id: 8289 };
      jest.spyOn(candidatFormService, 'getCandidat').mockReturnValue(candidat);
      jest.spyOn(candidatService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ candidat });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: candidat }));
      saveSubject.complete();

      // THEN
      expect(candidatFormService.getCandidat).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(candidatService.update).toHaveBeenCalledWith(expect.objectContaining(candidat));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICandidat>>();
      const candidat = { id: 8289 };
      jest.spyOn(candidatFormService, 'getCandidat').mockReturnValue({ id: null });
      jest.spyOn(candidatService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ candidat: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: candidat }));
      saveSubject.complete();

      // THEN
      expect(candidatFormService.getCandidat).toHaveBeenCalled();
      expect(candidatService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICandidat>>();
      const candidat = { id: 8289 };
      jest.spyOn(candidatService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ candidat });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(candidatService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareUser', () => {
      it('should forward to userService', () => {
        const entity = { id: 3944 };
        const entity2 = { id: 6275 };
        jest.spyOn(userService, 'compareUser');
        comp.compareUser(entity, entity2);
        expect(userService.compareUser).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareProfil', () => {
      it('should forward to profilService', () => {
        const entity = { id: 12279 };
        const entity2 = { id: 13621 };
        jest.spyOn(profilService, 'compareProfil');
        comp.compareProfil(entity, entity2);
        expect(profilService.compareProfil).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareDomaine', () => {
      it('should forward to domaineService', () => {
        const entity = { id: 14497 };
        const entity2 = { id: 24380 };
        jest.spyOn(domaineService, 'compareDomaine');
        comp.compareDomaine(entity, entity2);
        expect(domaineService.compareDomaine).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
