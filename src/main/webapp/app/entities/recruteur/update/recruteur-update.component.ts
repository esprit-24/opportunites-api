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
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/service/user.service';
import { IOrganisation } from 'app/entities/organisation/organisation.model';
import { OrganisationService } from 'app/entities/organisation/service/organisation.service';
import { RecruteurService } from '../service/recruteur.service';
import { IRecruteur } from '../recruteur.model';
import { RecruteurFormGroup, RecruteurFormService } from './recruteur-form.service';

@Component({
  selector: 'jhi-recruteur-update',
  templateUrl: './recruteur-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class RecruteurUpdateComponent implements OnInit {
  isSaving = false;
  recruteur: IRecruteur | null = null;

  usersSharedCollection: IUser[] = [];
  organisationsSharedCollection: IOrganisation[] = [];

  protected dataUtils = inject(DataUtils);
  protected eventManager = inject(EventManager);
  protected recruteurService = inject(RecruteurService);
  protected recruteurFormService = inject(RecruteurFormService);
  protected userService = inject(UserService);
  protected organisationService = inject(OrganisationService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: RecruteurFormGroup = this.recruteurFormService.createRecruteurFormGroup();

  compareUser = (o1: IUser | null, o2: IUser | null): boolean => this.userService.compareUser(o1, o2);

  compareOrganisation = (o1: IOrganisation | null, o2: IOrganisation | null): boolean =>
    this.organisationService.compareOrganisation(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ recruteur }) => {
      this.recruteur = recruteur;
      if (recruteur) {
        this.updateForm(recruteur);
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
    const recruteur = this.recruteurFormService.getRecruteur(this.editForm);
    if (recruteur.id !== null) {
      this.subscribeToSaveResponse(this.recruteurService.update(recruteur));
    } else {
      this.subscribeToSaveResponse(this.recruteurService.create(recruteur));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IRecruteur>>): void {
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

  protected updateForm(recruteur: IRecruteur): void {
    this.recruteur = recruteur;
    this.recruteurFormService.resetForm(this.editForm, recruteur);

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing<IUser>(this.usersSharedCollection, recruteur.user);
    this.organisationsSharedCollection = this.organisationService.addOrganisationToCollectionIfMissing<IOrganisation>(
      this.organisationsSharedCollection,
      recruteur.organisation,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing<IUser>(users, this.recruteur?.user)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));

    this.organisationService
      .query()
      .pipe(map((res: HttpResponse<IOrganisation[]>) => res.body ?? []))
      .pipe(
        map((organisations: IOrganisation[]) =>
          this.organisationService.addOrganisationToCollectionIfMissing<IOrganisation>(organisations, this.recruteur?.organisation),
        ),
      )
      .subscribe((organisations: IOrganisation[]) => (this.organisationsSharedCollection = organisations));
  }
}
