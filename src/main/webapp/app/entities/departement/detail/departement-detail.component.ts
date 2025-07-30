import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { IDepartement } from '../departement.model';

@Component({
  selector: 'jhi-departement-detail',
  templateUrl: './departement-detail.component.html',
  imports: [SharedModule, RouterModule],
})
export class DepartementDetailComponent {
  departement = input<IDepartement | null>(null);

  previousState(): void {
    window.history.back();
  }
}
