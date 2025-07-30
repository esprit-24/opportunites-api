import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../opportunite.test-samples';

import { OpportuniteFormService } from './opportunite-form.service';

describe('Opportunite Form Service', () => {
  let service: OpportuniteFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(OpportuniteFormService);
  });

  describe('Service methods', () => {
    describe('createOpportuniteFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createOpportuniteFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            titre: expect.any(Object),
            description: expect.any(Object),
            dateDebut: expect.any(Object),
            dateFin: expect.any(Object),
            adresse: expect.any(Object),
            niveauEtudeRequis: expect.any(Object),
            nombrePostes: expect.any(Object),
            salaire: expect.any(Object),
            statut: expect.any(Object),
            typeContrat: expect.any(Object),
            domaine: expect.any(Object),
            organisation: expect.any(Object),
            ville: expect.any(Object),
          }),
        );
      });

      it('passing IOpportunite should create a new form with FormGroup', () => {
        const formGroup = service.createOpportuniteFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            titre: expect.any(Object),
            description: expect.any(Object),
            dateDebut: expect.any(Object),
            dateFin: expect.any(Object),
            adresse: expect.any(Object),
            niveauEtudeRequis: expect.any(Object),
            nombrePostes: expect.any(Object),
            salaire: expect.any(Object),
            statut: expect.any(Object),
            typeContrat: expect.any(Object),
            domaine: expect.any(Object),
            organisation: expect.any(Object),
            ville: expect.any(Object),
          }),
        );
      });
    });

    describe('getOpportunite', () => {
      it('should return NewOpportunite for default Opportunite initial value', () => {
        const formGroup = service.createOpportuniteFormGroup(sampleWithNewData);

        const opportunite = service.getOpportunite(formGroup) as any;

        expect(opportunite).toMatchObject(sampleWithNewData);
      });

      it('should return NewOpportunite for empty Opportunite initial value', () => {
        const formGroup = service.createOpportuniteFormGroup();

        const opportunite = service.getOpportunite(formGroup) as any;

        expect(opportunite).toMatchObject({});
      });

      it('should return IOpportunite', () => {
        const formGroup = service.createOpportuniteFormGroup(sampleWithRequiredData);

        const opportunite = service.getOpportunite(formGroup) as any;

        expect(opportunite).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IOpportunite should not enable id FormControl', () => {
        const formGroup = service.createOpportuniteFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewOpportunite should disable id FormControl', () => {
        const formGroup = service.createOpportuniteFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
