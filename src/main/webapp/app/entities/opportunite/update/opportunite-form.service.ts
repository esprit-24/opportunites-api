import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IOpportunite, NewOpportunite } from '../opportunite.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IOpportunite for edit and NewOpportuniteFormGroupInput for create.
 */
type OpportuniteFormGroupInput = IOpportunite | PartialWithRequiredKeyOf<NewOpportunite>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IOpportunite | NewOpportunite> = Omit<T, 'dateDebut' | 'dateFin'> & {
  dateDebut?: string | null;
  dateFin?: string | null;
};

type OpportuniteFormRawValue = FormValueOf<IOpportunite>;

type NewOpportuniteFormRawValue = FormValueOf<NewOpportunite>;

type OpportuniteFormDefaults = Pick<NewOpportunite, 'id' | 'dateDebut' | 'dateFin'>;

type OpportuniteFormGroupContent = {
  id: FormControl<OpportuniteFormRawValue['id'] | NewOpportunite['id']>;
  titre: FormControl<OpportuniteFormRawValue['titre']>;
  description: FormControl<OpportuniteFormRawValue['description']>;
  dateDebut: FormControl<OpportuniteFormRawValue['dateDebut']>;
  dateFin: FormControl<OpportuniteFormRawValue['dateFin']>;
  adresse: FormControl<OpportuniteFormRawValue['adresse']>;
  niveauEtudeRequis: FormControl<OpportuniteFormRawValue['niveauEtudeRequis']>;
  nombrePostes: FormControl<OpportuniteFormRawValue['nombrePostes']>;
  salaire: FormControl<OpportuniteFormRawValue['salaire']>;
  statut: FormControl<OpportuniteFormRawValue['statut']>;
  typeContrat: FormControl<OpportuniteFormRawValue['typeContrat']>;
  domaine: FormControl<OpportuniteFormRawValue['domaine']>;
  organisation: FormControl<OpportuniteFormRawValue['organisation']>;
  ville: FormControl<OpportuniteFormRawValue['ville']>;
};

export type OpportuniteFormGroup = FormGroup<OpportuniteFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class OpportuniteFormService {
  createOpportuniteFormGroup(opportunite: OpportuniteFormGroupInput = { id: null }): OpportuniteFormGroup {
    const opportuniteRawValue = this.convertOpportuniteToOpportuniteRawValue({
      ...this.getFormDefaults(),
      ...opportunite,
    });
    return new FormGroup<OpportuniteFormGroupContent>({
      id: new FormControl(
        { value: opportuniteRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      titre: new FormControl(opportuniteRawValue.titre, {
        validators: [Validators.required],
      }),
      description: new FormControl(opportuniteRawValue.description, {
        validators: [Validators.required],
      }),
      dateDebut: new FormControl(opportuniteRawValue.dateDebut, {
        validators: [Validators.required],
      }),
      dateFin: new FormControl(opportuniteRawValue.dateFin),
      adresse: new FormControl(opportuniteRawValue.adresse),
      niveauEtudeRequis: new FormControl(opportuniteRawValue.niveauEtudeRequis),
      nombrePostes: new FormControl(opportuniteRawValue.nombrePostes, {
        validators: [Validators.required],
      }),
      salaire: new FormControl(opportuniteRawValue.salaire),
      statut: new FormControl(opportuniteRawValue.statut),
      typeContrat: new FormControl(opportuniteRawValue.typeContrat),
      domaine: new FormControl(opportuniteRawValue.domaine),
      organisation: new FormControl(opportuniteRawValue.organisation),
      ville: new FormControl(opportuniteRawValue.ville),
    });
  }

  getOpportunite(form: OpportuniteFormGroup): IOpportunite | NewOpportunite {
    return this.convertOpportuniteRawValueToOpportunite(form.getRawValue() as OpportuniteFormRawValue | NewOpportuniteFormRawValue);
  }

  resetForm(form: OpportuniteFormGroup, opportunite: OpportuniteFormGroupInput): void {
    const opportuniteRawValue = this.convertOpportuniteToOpportuniteRawValue({ ...this.getFormDefaults(), ...opportunite });
    form.reset(
      {
        ...opportuniteRawValue,
        id: { value: opportuniteRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): OpportuniteFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      dateDebut: currentTime,
      dateFin: currentTime,
    };
  }

  private convertOpportuniteRawValueToOpportunite(
    rawOpportunite: OpportuniteFormRawValue | NewOpportuniteFormRawValue,
  ): IOpportunite | NewOpportunite {
    return {
      ...rawOpportunite,
      dateDebut: dayjs(rawOpportunite.dateDebut, DATE_TIME_FORMAT),
      dateFin: dayjs(rawOpportunite.dateFin, DATE_TIME_FORMAT),
    };
  }

  private convertOpportuniteToOpportuniteRawValue(
    opportunite: IOpportunite | (Partial<NewOpportunite> & OpportuniteFormDefaults),
  ): OpportuniteFormRawValue | PartialWithRequiredKeyOf<NewOpportuniteFormRawValue> {
    return {
      ...opportunite,
      dateDebut: opportunite.dateDebut ? opportunite.dateDebut.format(DATE_TIME_FORMAT) : undefined,
      dateFin: opportunite.dateFin ? opportunite.dateFin.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
