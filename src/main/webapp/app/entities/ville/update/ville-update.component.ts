import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IDepartement } from 'app/entities/departement/departement.model';
import { DepartementService } from 'app/entities/departement/service/departement.service';
import { IVille } from '../ville.model';
import { VilleService } from '../service/ville.service';
import { VilleFormGroup, VilleFormService } from './ville-form.service';

@Component({
  selector: 'jhi-ville-update',
  templateUrl: './ville-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class VilleUpdateComponent implements OnInit {
  isSaving = false;
  ville: IVille | null = null;

  departementsSharedCollection: IDepartement[] = [];

  protected villeService = inject(VilleService);
  protected villeFormService = inject(VilleFormService);
  protected departementService = inject(DepartementService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: VilleFormGroup = this.villeFormService.createVilleFormGroup();

  compareDepartement = (o1: IDepartement | null, o2: IDepartement | null): boolean => this.departementService.compareDepartement(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ ville }) => {
      this.ville = ville;
      if (ville) {
        this.updateForm(ville);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const ville = this.villeFormService.getVille(this.editForm);
    if (ville.id !== null) {
      this.subscribeToSaveResponse(this.villeService.update(ville));
    } else {
      this.subscribeToSaveResponse(this.villeService.create(ville));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IVille>>): void {
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

  protected updateForm(ville: IVille): void {
    this.ville = ville;
    this.villeFormService.resetForm(this.editForm, ville);

    this.departementsSharedCollection = this.departementService.addDepartementToCollectionIfMissing<IDepartement>(
      this.departementsSharedCollection,
      ville.departement,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.departementService
      .query()
      .pipe(map((res: HttpResponse<IDepartement[]>) => res.body ?? []))
      .pipe(
        map((departements: IDepartement[]) =>
          this.departementService.addDepartementToCollectionIfMissing<IDepartement>(departements, this.ville?.departement),
        ),
      )
      .subscribe((departements: IDepartement[]) => (this.departementsSharedCollection = departements));
  }
}
