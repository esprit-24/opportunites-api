import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../domaine.test-samples';

import { DomaineFormService } from './domaine-form.service';

describe('Domaine Form Service', () => {
  let service: DomaineFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(DomaineFormService);
  });

  describe('Service methods', () => {
    describe('createDomaineFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createDomaineFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            intitule: expect.any(Object),
            description: expect.any(Object),
          }),
        );
      });

      it('passing IDomaine should create a new form with FormGroup', () => {
        const formGroup = service.createDomaineFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            intitule: expect.any(Object),
            description: expect.any(Object),
          }),
        );
      });
    });

    describe('getDomaine', () => {
      it('should return NewDomaine for default Domaine initial value', () => {
        const formGroup = service.createDomaineFormGroup(sampleWithNewData);

        const domaine = service.getDomaine(formGroup) as any;

        expect(domaine).toMatchObject(sampleWithNewData);
      });

      it('should return NewDomaine for empty Domaine initial value', () => {
        const formGroup = service.createDomaineFormGroup();

        const domaine = service.getDomaine(formGroup) as any;

        expect(domaine).toMatchObject({});
      });

      it('should return IDomaine', () => {
        const formGroup = service.createDomaineFormGroup(sampleWithRequiredData);

        const domaine = service.getDomaine(formGroup) as any;

        expect(domaine).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IDomaine should not enable id FormControl', () => {
        const formGroup = service.createDomaineFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewDomaine should disable id FormControl', () => {
        const formGroup = service.createDomaineFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
