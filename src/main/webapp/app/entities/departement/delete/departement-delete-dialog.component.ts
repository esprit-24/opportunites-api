import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IDepartement } from '../departement.model';
import { DepartementService } from '../service/departement.service';

@Component({
  templateUrl: './departement-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class DepartementDeleteDialogComponent {
  departement?: IDepartement;

  protected departementService = inject(DepartementService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.departementService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
