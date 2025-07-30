import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { IVille } from '../ville.model';

@Component({
  selector: 'jhi-ville-detail',
  templateUrl: './ville-detail.component.html',
  imports: [SharedModule, RouterModule],
})
export class VilleDetailComponent {
  ville = input<IVille | null>(null);

  previousState(): void {
    window.history.back();
  }
}
