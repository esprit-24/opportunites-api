import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IRegion } from 'app/entities/region/region.model';
import { RegionService } from 'app/entities/region/service/region.service';
import { IDepartement } from '../departement.model';
import { DepartementService } from '../service/departement.service';
import { DepartementFormGroup, DepartementFormService } from './departement-form.service';

@Component({
  selector: 'jhi-departement-update',
  templateUrl: './departement-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class DepartementUpdateComponent implements OnInit {
  isSaving = false;
  departement: IDepartement | null = null;

  regionsSharedCollection: IRegion[] = [];

  protected departementService = inject(DepartementService);
  protected departementFormService = inject(DepartementFormService);
  protected regionService = inject(RegionService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: DepartementFormGroup = this.departementFormService.createDepartementFormGroup();

  compareRegion = (o1: IRegion | null, o2: IRegion | null): boolean => this.regionService.compareRegion(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ departement }) => {
      this.departement = departement;
      if (departement) {
        this.updateForm(departement);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const departement = this.departementFormService.getDepartement(this.editForm);
    if (departement.id !== null) {
      this.subscribeToSaveResponse(this.departementService.update(departement));
    } else {
      this.subscribeToSaveResponse(this.departementService.create(departement));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDepartement>>): void {
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

  protected updateForm(departement: IDepartement): void {
    this.departement = departement;
    this.departementFormService.resetForm(this.editForm, departement);

    this.regionsSharedCollection = this.regionService.addRegionToCollectionIfMissing<IRegion>(
      this.regionsSharedCollection,
      departement.region,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.regionService
      .query()
      .pipe(map((res: HttpResponse<IRegion[]>) => res.body ?? []))
      .pipe(map((regions: IRegion[]) => this.regionService.addRegionToCollectionIfMissing<IRegion>(regions, this.departement?.region)))
      .subscribe((regions: IRegion[]) => (this.regionsSharedCollection = regions));
  }
}
