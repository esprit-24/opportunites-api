import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IDomaine } from 'app/entities/domaine/domaine.model';
import { DomaineService } from 'app/entities/domaine/service/domaine.service';
import { ProfilService } from '../service/profil.service';
import { IProfil } from '../profil.model';
import { ProfilFormService } from './profil-form.service';

import { ProfilUpdateComponent } from './profil-update.component';

describe('Profil Management Update Component', () => {
  let comp: ProfilUpdateComponent;
  let fixture: ComponentFixture<ProfilUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let profilFormService: ProfilFormService;
  let profilService: ProfilService;
  let domaineService: DomaineService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [ProfilUpdateComponent],
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
      .overrideTemplate(ProfilUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ProfilUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    profilFormService = TestBed.inject(ProfilFormService);
    profilService = TestBed.inject(ProfilService);
    domaineService = TestBed.inject(DomaineService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call Domaine query and add missing value', () => {
      const profil: IProfil = { id: 13621 };
      const domaine: IDomaine = { id: 14497 };
      profil.domaine = domaine;

      const domaineCollection: IDomaine[] = [{ id: 14497 }];
      jest.spyOn(domaineService, 'query').mockReturnValue(of(new HttpResponse({ body: domaineCollection })));
      const additionalDomaines = [domaine];
      const expectedCollection: IDomaine[] = [...additionalDomaines, ...domaineCollection];
      jest.spyOn(domaineService, 'addDomaineToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ profil });
      comp.ngOnInit();

      expect(domaineService.query).toHaveBeenCalled();
      expect(domaineService.addDomaineToCollectionIfMissing).toHaveBeenCalledWith(
        domaineCollection,
        ...additionalDomaines.map(expect.objectContaining),
      );
      expect(comp.domainesSharedCollection).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const profil: IProfil = { id: 13621 };
      const domaine: IDomaine = { id: 14497 };
      profil.domaine = domaine;

      activatedRoute.data = of({ profil });
      comp.ngOnInit();

      expect(comp.domainesSharedCollection).toContainEqual(domaine);
      expect(comp.profil).toEqual(profil);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IProfil>>();
      const profil = { id: 12279 };
      jest.spyOn(profilFormService, 'getProfil').mockReturnValue(profil);
      jest.spyOn(profilService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ profil });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: profil }));
      saveSubject.complete();

      // THEN
      expect(profilFormService.getProfil).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(profilService.update).toHaveBeenCalledWith(expect.objectContaining(profil));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IProfil>>();
      const profil = { id: 12279 };
      jest.spyOn(profilFormService, 'getProfil').mockReturnValue({ id: null });
      jest.spyOn(profilService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ profil: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: profil }));
      saveSubject.complete();

      // THEN
      expect(profilFormService.getProfil).toHaveBeenCalled();
      expect(profilService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IProfil>>();
      const profil = { id: 12279 };
      jest.spyOn(profilService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ profil });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(profilService.update).toHaveBeenCalled();
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
  });
});
