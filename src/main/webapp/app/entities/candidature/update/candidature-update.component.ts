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
import { IOpportunite } from 'app/entities/opportunite/opportunite.model';
import { OpportuniteService } from 'app/entities/opportunite/service/opportunite.service';
import { ICandidat } from 'app/entities/candidat/candidat.model';
import { CandidatService } from 'app/entities/candidat/service/candidat.service';
import { StatutCandidature } from 'app/entities/enumerations/statut-candidature.model';
import { CandidatureService } from '../service/candidature.service';
import { ICandidature } from '../candidature.model';
import { CandidatureFormGroup, CandidatureFormService } from './candidature-form.service';

@Component({
  selector: 'jhi-candidature-update',
  templateUrl: './candidature-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class CandidatureUpdateComponent implements OnInit {
  isSaving = false;
  candidature: ICandidature | null = null;
  statutCandidatureValues = Object.keys(StatutCandidature);

  opportunitesSharedCollection: IOpportunite[] = [];
  candidatsSharedCollection: ICandidat[] = [];

  protected dataUtils = inject(DataUtils);
  protected eventManager = inject(EventManager);
  protected candidatureService = inject(CandidatureService);
  protected candidatureFormService = inject(CandidatureFormService);
  protected opportuniteService = inject(OpportuniteService);
  protected candidatService = inject(CandidatService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: CandidatureFormGroup = this.candidatureFormService.createCandidatureFormGroup();

  compareOpportunite = (o1: IOpportunite | null, o2: IOpportunite | null): boolean => this.opportuniteService.compareOpportunite(o1, o2);

  compareCandidat = (o1: ICandidat | null, o2: ICandidat | null): boolean => this.candidatService.compareCandidat(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ candidature }) => {
      this.candidature = candidature;
      if (candidature) {
        this.updateForm(candidature);
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
    const candidature = this.candidatureFormService.getCandidature(this.editForm);
    if (candidature.id !== null) {
      this.subscribeToSaveResponse(this.candidatureService.update(candidature));
    } else {
      this.subscribeToSaveResponse(this.candidatureService.create(candidature));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICandidature>>): void {
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

  protected updateForm(candidature: ICandidature): void {
    this.candidature = candidature;
    this.candidatureFormService.resetForm(this.editForm, candidature);

    this.opportunitesSharedCollection = this.opportuniteService.addOpportuniteToCollectionIfMissing<IOpportunite>(
      this.opportunitesSharedCollection,
      candidature.opportunite,
    );
    this.candidatsSharedCollection = this.candidatService.addCandidatToCollectionIfMissing<ICandidat>(
      this.candidatsSharedCollection,
      candidature.candidat,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.opportuniteService
      .query()
      .pipe(map((res: HttpResponse<IOpportunite[]>) => res.body ?? []))
      .pipe(
        map((opportunites: IOpportunite[]) =>
          this.opportuniteService.addOpportuniteToCollectionIfMissing<IOpportunite>(opportunites, this.candidature?.opportunite),
        ),
      )
      .subscribe((opportunites: IOpportunite[]) => (this.opportunitesSharedCollection = opportunites));

    this.candidatService
      .query()
      .pipe(map((res: HttpResponse<ICandidat[]>) => res.body ?? []))
      .pipe(
        map((candidats: ICandidat[]) =>
          this.candidatService.addCandidatToCollectionIfMissing<ICandidat>(candidats, this.candidature?.candidat),
        ),
      )
      .subscribe((candidats: ICandidat[]) => (this.candidatsSharedCollection = candidats));
  }
}
