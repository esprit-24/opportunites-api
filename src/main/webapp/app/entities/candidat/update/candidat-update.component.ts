import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/service/user.service';
import { IProfil } from 'app/entities/profil/profil.model';
import { ProfilService } from 'app/entities/profil/service/profil.service';
import { IDomaine } from 'app/entities/domaine/domaine.model';
import { DomaineService } from 'app/entities/domaine/service/domaine.service';
import { NiveauEtude } from 'app/entities/enumerations/niveau-etude.model';
import { CandidatService } from '../service/candidat.service';
import { ICandidat } from '../candidat.model';
import { CandidatFormGroup, CandidatFormService } from './candidat-form.service';

@Component({
  selector: 'jhi-candidat-update',
  templateUrl: './candidat-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class CandidatUpdateComponent implements OnInit {
  isSaving = false;
  candidat: ICandidat | null = null;
  niveauEtudeValues = Object.keys(NiveauEtude);

  usersSharedCollection: IUser[] = [];
  profilsSharedCollection: IProfil[] = [];
  domainesSharedCollection: IDomaine[] = [];

  protected candidatService = inject(CandidatService);
  protected candidatFormService = inject(CandidatFormService);
  protected userService = inject(UserService);
  protected profilService = inject(ProfilService);
  protected domaineService = inject(DomaineService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: CandidatFormGroup = this.candidatFormService.createCandidatFormGroup();

  compareUser = (o1: IUser | null, o2: IUser | null): boolean => this.userService.compareUser(o1, o2);

  compareProfil = (o1: IProfil | null, o2: IProfil | null): boolean => this.profilService.compareProfil(o1, o2);

  compareDomaine = (o1: IDomaine | null, o2: IDomaine | null): boolean => this.domaineService.compareDomaine(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ candidat }) => {
      this.candidat = candidat;
      if (candidat) {
        this.updateForm(candidat);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const candidat = this.candidatFormService.getCandidat(this.editForm);
    if (candidat.id !== null) {
      this.subscribeToSaveResponse(this.candidatService.update(candidat));
    } else {
      this.subscribeToSaveResponse(this.candidatService.create(candidat));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICandidat>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(candidat: ICandidat): void {
    this.candidat = candidat;
    this.candidatFormService.resetForm(this.editForm, candidat);

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing<IUser>(this.usersSharedCollection, candidat.user);
    this.profilsSharedCollection = this.profilService.addProfilToCollectionIfMissing<IProfil>(
      this.profilsSharedCollection,
      candidat.profil,
    );
    this.domainesSharedCollection = this.domaineService.addDomaineToCollectionIfMissing<IDomaine>(
      this.domainesSharedCollection,
      candidat.domaine,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing<IUser>(users, this.candidat?.user)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));

    this.profilService
      .query()
      .pipe(map((res: HttpResponse<IProfil[]>) => res.body ?? []))
      .pipe(map((profils: IProfil[]) => this.profilService.addProfilToCollectionIfMissing<IProfil>(profils, this.candidat?.profil)))
      .subscribe((profils: IProfil[]) => (this.profilsSharedCollection = profils));

    this.domaineService
      .query()
      .pipe(map((res: HttpResponse<IDomaine[]>) => res.body ?? []))
      .pipe(map((domaines: IDomaine[]) => this.domaineService.addDomaineToCollectionIfMissing<IDomaine>(domaines, this.candidat?.domaine)))
      .subscribe((domaines: IDomaine[]) => (this.domainesSharedCollection = domaines));
  }
}
