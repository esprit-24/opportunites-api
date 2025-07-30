import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { IProfil } from '../profil.model';

@Component({
  selector: 'jhi-profil-detail',
  templateUrl: './profil-detail.component.html',
  imports: [SharedModule, RouterModule],
})
export class ProfilDetailComponent {
  profil = input<IProfil | null>(null);

  previousState(): void {
    window.history.back();
  }
}
