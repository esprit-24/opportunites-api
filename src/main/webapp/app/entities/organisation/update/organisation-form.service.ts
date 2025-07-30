import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IOrganisation, NewOrganisation } from '../organisation.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IOrganisation for edit and NewOrganisationFormGroupInput for create.
 */
type OrganisationFormGroupInput = IOrganisation | PartialWithRequiredKeyOf<NewOrganisation>;

type OrganisationFormDefaults = Pick<NewOrganisation, 'id'>;

type OrganisationFormGroupContent = {
  id: FormControl<IOrganisation['id'] | NewOrganisation['id']>;
  nom: FormControl<IOrganisation['nom']>;
  presentation: FormControl<IOrganisation['presentation']>;
  secteurActivite: FormControl<IOrganisation['secteurActivite']>;
  logoUrl: FormControl<IOrganisation['logoUrl']>;
  adresse: FormControl<IOrganisation['adresse']>;
  siteWeb: FormControl<IOrganisation['siteWeb']>;
  emailContact: FormControl<IOrganisation['emailContact']>;
  telephone: FormControl<IOrganisation['telephone']>;
  ville: FormControl<IOrganisation['ville']>;
};

export type OrganisationFormGroup = FormGroup<OrganisationFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class OrganisationFormService {
  createOrganisationFormGroup(organisation: OrganisationFormGroupInput = { id: null }): OrganisationFormGroup {
    const organisationRawValue = {
      ...this.getFormDefaults(),
      ...organisation,
    };
    return new FormGroup<OrganisationFormGroupContent>({
      id: new FormControl(
        { value: organisationRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      nom: new FormControl(organisationRawValue.nom, {
        validators: [Validators.required],
      }),
      presentation: new FormControl(organisationRawValue.presentation, {
        validators: [Validators.required],
      }),
      secteurActivite: new FormControl(organisationRawValue.secteurActivite),
      logoUrl: new FormControl(organisationRawValue.logoUrl),
      adresse: new FormControl(organisationRawValue.adresse, {
        validators: [Validators.required],
      }),
      siteWeb: new FormControl(organisationRawValue.siteWeb),
      emailContact: new FormControl(organisationRawValue.emailContact, {
        validators: [Validators.required],
      }),
      telephone: new FormControl(organisationRawValue.telephone),
      ville: new FormControl(organisationRawValue.ville),
    });
  }

  getOrganisation(form: OrganisationFormGroup): IOrganisation | NewOrganisation {
    return form.getRawValue() as IOrganisation | NewOrganisation;
  }

  resetForm(form: OrganisationFormGroup, organisation: OrganisationFormGroupInput): void {
    const organisationRawValue = { ...this.getFormDefaults(), ...organisation };
    form.reset(
      {
        ...organisationRawValue,
        id: { value: organisationRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): OrganisationFormDefaults {
    return {
      id: null,
    };
  }
}
