import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { ProfilDetailComponent } from './profil-detail.component';

describe('Profil Management Detail Component', () => {
  let comp: ProfilDetailComponent;
  let fixture: ComponentFixture<ProfilDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ProfilDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./profil-detail.component').then(m => m.ProfilDetailComponent),
              resolve: { profil: () => of({ id: 12279 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(ProfilDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ProfilDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load profil on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', ProfilDetailComponent);

      // THEN
      expect(instance.profil()).toEqual(expect.objectContaining({ id: 12279 }));
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
