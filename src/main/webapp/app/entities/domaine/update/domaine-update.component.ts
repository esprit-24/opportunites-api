import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { DomaineService } from '../service/domaine.service';
import { IDomaine } from '../domaine.model';
import { DomaineFormGroup, DomaineFormService } from './domaine-form.service';

@Component({
  selector: 'jhi-domaine-update',
  templateUrl: './domaine-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class DomaineUpdateComponent implements OnInit {
  isSaving = false;
  domaine: IDomaine | null = null;

  protected dataUtils = inject(DataUtils);
  protected eventManager = inject(EventManager);
  protected domaineService = inject(DomaineService);
  protected domaineFormService = inject(DomaineFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: DomaineFormGroup = this.domaineFormService.createDomaineFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ domaine }) => {
      this.domaine = domaine;
      if (domaine) {
        this.updateForm(domaine);
      }
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
    const domaine = this.domaineFormService.getDomaine(this.editForm);
    if (domaine.id !== null) {
      this.subscribeToSaveResponse(this.domaineService.update(domaine));
    } else {
      this.subscribeToSaveResponse(this.domaineService.create(domaine));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDomaine>>): void {
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

  protected updateForm(domaine: IDomaine): void {
    this.domaine = domaine;
    this.domaineFormService.resetForm(this.editForm, domaine);
  }
}
