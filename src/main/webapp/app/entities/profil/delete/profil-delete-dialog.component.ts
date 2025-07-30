import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IProfil } from '../profil.model';
import { ProfilService } from '../service/profil.service';

@Component({
  templateUrl: './profil-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class ProfilDeleteDialogComponent {
  profil?: IProfil;

  protected profilService = inject(ProfilService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.profilService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
