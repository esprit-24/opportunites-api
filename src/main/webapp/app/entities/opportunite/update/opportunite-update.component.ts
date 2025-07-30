import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IDomaine } from 'app/entities/domaine/domaine.model';
import { DomaineService } from 'app/entities/domaine/service/domaine.service';
import { IOrganisation } from 'app/entities/organisation/organisation.model';
import { OrganisationService } from 'app/entities/organisation/service/organisation.service';
import { IVille } from 'app/entities/ville/ville.model';
import { VilleService } from 'app/entities/ville/service/ville.service';
import { NiveauEtude } from 'app/entities/enumerations/niveau-etude.model';
import { Statut } from 'app/entities/enumerations/statut.model';
import { TypeContrat } from 'app/entities/enumerations/type-contrat.model';
import { OpportuniteService } from '../service/opportunite.service';
import { IOpportunite } from '../opportunite.model';
import { OpportuniteFormGroup, OpportuniteFormService } from './opportunite-form.service';

@Component({
  selector: 'jhi-opportunite-update',
  templateUrl: './opportunite-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class OpportuniteUpdateComponent implements OnInit {
  isSaving = false;
  opportunite: IOpportunite | null = null;
  niveauEtudeValues = Object.keys(NiveauEtude);
  statutValues = Object.keys(Statut);
  typeContratValues = Object.keys(TypeContrat);

  domainesSharedCollection: IDomaine[] = [];
  organisationsSharedCollection: IOrganisation[] = [];
  villesSharedCollection: IVille[] = [];

  protected dataUtils = inject(DataUtils);
  protected eventManager = inject(EventManager);
  protected opportuniteService = inject(OpportuniteService);
  protected opportuniteFormService = inject(OpportuniteFormService);
  protected domaineService = inject(DomaineService);
  protected organisationService = inject(OrganisationService);
  protected villeService = inject(VilleService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: OpportuniteFormGroup = this.opportuniteFormService.createOpportuniteFormGroup();

  compareDomaine = (o1: IDomaine | null, o2: IDomaine | null): boolean => this.domaineService.compareDomaine(o1, o2);

  compareOrganisation = (o1: IOrganisation | null, o2: IOrganisation | null): boolean =>
    this.organisationService.compareOrganisation(o1, o2);

  compareVille = (o1: IVille | null, o2: IVille | null): boolean => this.villeService.compareVille(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ opportunite }) => {
      this.opportunite = opportunite;
      if (opportunite) {
        this.updateForm(opportunite);
      }

      this.loadRelationshipsOptions();
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe({
      error: (err: FileLoadError) =>
        this.eventManager.broadcast(new EventWithContent<AlertError>('opportunitesApp.error', { ...err, key: `error.file.${err.key}` })),
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const opportunite = this.opportuniteFormService.getOpportunite(this.editForm);
    if (opportunite.id !== null) {
      this.subscribeToSaveResponse(this.opportuniteService.update(opportunite));
    } else {
      this.subscribeToSaveResponse(this.opportuniteService.create(opportunite));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IOpportunite>>): void {
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

  protected updateForm(opportunite: IOpportunite): void {
    this.opportunite = opportunite;
    this.opportuniteFormService.resetForm(this.editForm, opportunite);

    this.domainesSharedCollection = this.domaineService.addDomaineToCollectionIfMissing<IDomaine>(
      this.domainesSharedCollection,
      opportunite.domaine,
    );
    this.organisationsSharedCollection = this.organisationService.addOrganisationToCollectionIfMissing<IOrganisation>(
      this.organisationsSharedCollection,
      opportunite.organisation,
    );
    this.villesSharedCollection = this.villeService.addVilleToCollectionIfMissing<IVille>(this.villesSharedCollection, opportunite.ville);
  }

  protected loadRelationshipsOptions(): void {
    this.domaineService
      .query()
      .pipe(map((res: HttpResponse<IDomaine[]>) => res.body ?? []))
      .pipe(
        map((domaines: IDomaine[]) => this.domaineService.addDomaineToCollectionIfMissing<IDomaine>(domaines, this.opportunite?.domaine)),
      )
      .subscribe((domaines: IDomaine[]) => (this.domainesSharedCollection = domaines));

    this.organisationService
      .query()
      .pipe(map((res: HttpResponse<IOrganisation[]>) => res.body ?? []))
      .pipe(
        map((organisations: IOrganisation[]) =>
          this.organisationService.addOrganisationToCollectionIfMissing<IOrganisation>(organisations, this.opportunite?.organisation),
        ),
      )
      .subscribe((organisations: IOrganisation[]) => (this.organisationsSharedCollection = organisations));

    this.villeService
      .query()
      .pipe(map((res: HttpResponse<IVille[]>) => res.body ?? []))
      .pipe(map((villes: IVille[]) => this.villeService.addVilleToCollectionIfMissing<IVille>(villes, this.opportunite?.ville)))
      .subscribe((villes: IVille[]) => (this.villesSharedCollection = villes));
  }
}
