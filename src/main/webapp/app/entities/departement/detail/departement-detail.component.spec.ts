import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { DepartementDetailComponent } from './departement-detail.component';

describe('Departement Management Detail Component', () => {
  let comp: DepartementDetailComponent;
  let fixture: ComponentFixture<DepartementDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DepartementDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./departement-detail.component').then(m => m.DepartementDetailComponent),
              resolve: { departement: () => of({ id: 13725 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(DepartementDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(DepartementDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load departement on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', DepartementDetailComponent);

      // THEN
      expect(instance.departement()).toEqual(expect.objectContaining({ id: 13725 }));
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
