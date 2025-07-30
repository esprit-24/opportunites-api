import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IDomaine, NewDomaine } from '../domaine.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IDomaine for edit and NewDomaineFormGroupInput for create.
 */
type DomaineFormGroupInput = IDomaine | PartialWithRequiredKeyOf<NewDomaine>;

type DomaineFormDefaults = Pick<NewDomaine, 'id'>;

type DomaineFormGroupContent = {
  id: FormControl<IDomaine['id'] | NewDomaine['id']>;
  intitule: FormControl<IDomaine['intitule']>;
  description: FormControl<IDomaine['description']>;
};

export type DomaineFormGroup = FormGroup<DomaineFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class DomaineFormService {
  createDomaineFormGroup(domaine: DomaineFormGroupInput = { id: null }): DomaineFormGroup {
    const domaineRawValue = {
      ...this.getFormDefaults(),
      ...domaine,
    };
    return new FormGroup<DomaineFormGroupContent>({
      id: new FormControl(
        { value: domaineRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      intitule: new FormControl(domaineRawValue.intitule, {
        validators: [Validators.required],
      }),
      description: new FormControl(domaineRawValue.description, {
        validators: [Validators.required],
      }),
    });
  }

  getDomaine(form: DomaineFormGroup): IDomaine | NewDomaine {
    return form.getRawValue() as IDomaine | NewDomaine;
  }

  resetForm(form: DomaineFormGroup, domaine: DomaineFormGroupInput): void {
    const domaineRawValue = { ...this.getFormDefaults(), ...domaine };
    form.reset(
      {
        ...domaineRawValue,
        id: { value: domaineRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): DomaineFormDefaults {
    return {
      id: null,
    };
  }
}
