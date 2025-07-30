import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IDomaine } from 'app/entities/domaine/domaine.model';
import { DomaineService } from 'app/entities/domaine/service/domaine.service';
import { IProfil } from '../profil.model';
import { ProfilService } from '../service/profil.service';
import { ProfilFormGroup, ProfilFormService } from './profil-form.service';

@Component({
  selector: 'jhi-profil-update',
  templateUrl: './profil-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ProfilUpdateComponent implements OnInit {
  isSaving = false;
  profil: IProfil | null = null;

  domainesSharedCollection: IDomaine[] = [];

  protected profilService = inject(ProfilService);
  protected profilFormService = inject(ProfilFormService);
  protected domaineService = inject(DomaineService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ProfilFormGroup = this.profilFormService.createProfilFormGroup();

  compareDomaine = (o1: IDomaine | null, o2: IDomaine | null): boolean => this.domaineService.compareDomaine(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ profil }) => {
      this.profil = profil;
      if (profil) {
        this.updateForm(profil);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const profil = this.profilFormService.getProfil(this.editForm);
    if (profil.id !== null) {
      this.subscribeToSaveResponse(this.profilService.update(profil));
    } else {
      this.subscribeToSaveResponse(this.profilService.create(profil));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IProfil>>): void {
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

  protected updateForm(profil: IProfil): void {
    this.profil = profil;
    this.profilFormService.resetForm(this.editForm, profil);

    this.domainesSharedCollection = this.domaineService.addDomaineToCollectionIfMissing<IDomaine>(
      this.domainesSharedCollection,
      profil.domaine,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.domaineService
      .query()
      .pipe(map((res: HttpResponse<IDomaine[]>) => res.body ?? []))
      .pipe(map((domaines: IDomaine[]) => this.domaineService.addDomaineToCollectionIfMissing<IDomaine>(domaines, this.profil?.domaine)))
      .subscribe((domaines: IDomaine[]) => (this.domainesSharedCollection = domaines));
  }
}
