import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { VilleDetailComponent } from './ville-detail.component';

describe('Ville Management Detail Component', () => {
  let comp: VilleDetailComponent;
  let fixture: ComponentFixture<VilleDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [VilleDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./ville-detail.component').then(m => m.VilleDetailComponent),
              resolve: { ville: () => of({ id: 13650 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(VilleDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(VilleDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load ville on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', VilleDetailComponent);

      // THEN
      expect(instance.ville()).toEqual(expect.objectContaining({ id: 13650 }));
    });
  });

  describe('PreviousState', () => {
    it('should navigate to previous state', () => {
      jest.spyOn(window.history, 'back');
      comp.previousState();
      expect(window.history.back).toHaveBeenCalled();
    });
  });
});
