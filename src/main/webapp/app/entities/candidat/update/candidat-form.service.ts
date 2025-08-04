import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ICandidat, NewCandidat } from '../candidat.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ICandidat for edit and NewCandidatFormGroupInput for create.
 */
type CandidatFormGroupInput = ICandidat | PartialWithRequiredKeyOf<NewCandidat>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends ICandidat | NewCandidat> = Omit<T, 'dateNaissance'> & {
  dateNaissance?: string | null;
};

type CandidatFormRawValue = FormValueOf<ICandidat>;

type NewCandidatFormRawValue = FormValueOf<NewCandidat>;

type CandidatFormDefaults = Pick<NewCandidat, 'id' | 'dateNaissance'>;

type CandidatFormGroupContent = {
  id: FormControl<CandidatFormRawValue['id'] | NewCandidat['id']>;
  dateNaissance: FormControl<CandidatFormRawValue['dateNaissance']>;
  niveauEtude: FormControl<CandidatFormRawValue['niveauEtude']>;
  cvUrl: FormControl<CandidatFormRawValue['cvUrl']>;
  statutActuel: FormControl<CandidatFormRawValue['statutActuel']>;
  user: FormControl<CandidatFormRawValue['user']>;
  profil: FormControl<CandidatFormRawValue['profil']>;
};

export type CandidatFormGroup = FormGroup<CandidatFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class CandidatFormService {
  createCandidatFormGroup(candidat: CandidatFormGroupInput = { id: null }): CandidatFormGroup {
    const candidatRawValue = this.convertCandidatToCandidatRawValue({
      ...this.getFormDefaults(),
      ...candidat,
    });
    return new FormGroup<CandidatFormGroupContent>({
      id: new FormControl(
        { value: candidatRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      dateNaissance: new FormControl(candidatRawValue.dateNaissance),
      niveauEtude: new FormControl(candidatRawValue.niveauEtude, {
        validators: [Validators.required],
      }),
      cvUrl: new FormControl(candidatRawValue.cvUrl),
      statutActuel: new FormControl(candidatRawValue.statutActuel),
      user: new FormControl(candidatRawValue.user),
      profil: new FormControl(candidatRawValue.profil),
    });
  }

  getCandidat(form: CandidatFormGroup): ICandidat | NewCandidat {
    return this.convertCandidatRawValueToCandidat(form.getRawValue() as CandidatFormRawValue | NewCandidatFormRawValue);
  }

  resetForm(form: CandidatFormGroup, candidat: CandidatFormGroupInput): void {
    const candidatRawValue = this.convertCandidatToCandidatRawValue({ ...this.getFormDefaults(), ...candidat });
    form.reset(
      {
        ...candidatRawValue,
        id: { value: candidatRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): CandidatFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      dateNaissance: currentTime,
    };
  }

  private convertCandidatRawValueToCandidat(rawCandidat: CandidatFormRawValue | NewCandidatFormRawValue): ICandidat | NewCandidat {
    return {
      ...rawCandidat,
      dateNaissance: dayjs(rawCandidat.dateNaissance, DATE_TIME_FORMAT),
    };
  }

  private convertCandidatToCandidatRawValue(
    candidat: ICandidat | (Partial<NewCandidat> & CandidatFormDefaults),
  ): CandidatFormRawValue | PartialWithRequiredKeyOf<NewCandidatFormRawValue> {
    return {
      ...candidat,
      dateNaissance: candidat.dateNaissance ? candidat.dateNaissance.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
