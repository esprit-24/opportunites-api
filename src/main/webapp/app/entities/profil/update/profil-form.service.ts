import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IProfil, NewProfil } from '../profil.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IProfil for edit and NewProfilFormGroupInput for create.
 */
type ProfilFormGroupInput = IProfil | PartialWithRequiredKeyOf<NewProfil>;

type ProfilFormDefaults = Pick<NewProfil, 'id'>;

type ProfilFormGroupContent = {
  id: FormControl<IProfil['id'] | NewProfil['id']>;
  intitule: FormControl<IProfil['intitule']>;
  domaine: FormControl<IProfil['domaine']>;
};

export type ProfilFormGroup = FormGroup<ProfilFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ProfilFormService {
  createProfilFormGroup(profil: ProfilFormGroupInput = { id: null }): ProfilFormGroup {
    const profilRawValue = {
      ...this.getFormDefaults(),
      ...profil,
    };
    return new FormGroup<ProfilFormGroupContent>({
      id: new FormControl(
        { value: profilRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      intitule: new FormControl(profilRawValue.intitule, {
        validators: [Validators.required],
      }),
      domaine: new FormControl(profilRawValue.domaine),
    });
  }

  getProfil(form: ProfilFormGroup): IProfil | NewProfil {
    return form.getRawValue() as IProfil | NewProfil;
  }

  resetForm(form: ProfilFormGroup, profil: ProfilFormGroupInput): void {
    const profilRawValue = { ...this.getFormDefaults(), ...profil };
    form.reset(
      {
        ...profilRawValue,
        id: { value: profilRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): ProfilFormDefaults {
    return {
      id: null,
    };
  }
}
