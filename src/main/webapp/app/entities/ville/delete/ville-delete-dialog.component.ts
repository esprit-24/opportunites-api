import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IVille } from '../ville.model';
import { VilleService } from '../service/ville.service';

@Component({
  templateUrl: './ville-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class VilleDeleteDialogComponent {
  ville?: IVille;

  protected villeService = inject(VilleService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.villeService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
