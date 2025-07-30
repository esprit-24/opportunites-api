import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../profil.test-samples';

import { ProfilFormService } from './profil-form.service';

describe('Profil Form Service', () => {
  let service: ProfilFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ProfilFormService);
  });

  describe('Service methods', () => {
    describe('createProfilFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createProfilFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            intitule: expect.any(Object),
            domaine: expect.any(Object),
          }),
        );
      });

      it('passing IProfil should create a new form with FormGroup', () => {
        const formGroup = service.createProfilFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            intitule: expect.any(Object),
            domaine: expect.any(Object),
          }),
        );
      });
    });

    describe('getProfil', () => {
      it('should return NewProfil for default Profil initial value', () => {
        const formGroup = service.createProfilFormGroup(sampleWithNewData);

        const profil = service.getProfil(formGroup) as any;

        expect(profil).toMatchObject(sampleWithNewData);
      });

      it('should return NewProfil for empty Profil initial value', () => {
        const formGroup = service.createProfilFormGroup();

        const profil = service.getProfil(formGroup) as any;

        expect(profil).toMatchObject({});
      });

      it('should return IProfil', () => {
        const formGroup = service.createProfilFormGroup(sampleWithRequiredData);

        const profil = service.getProfil(formGroup) as any;

        expect(profil).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IProfil should not enable id FormControl', () => {
        const formGroup = service.createProfilFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewProfil should disable id FormControl', () => {
        const formGroup = service.createProfilFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
