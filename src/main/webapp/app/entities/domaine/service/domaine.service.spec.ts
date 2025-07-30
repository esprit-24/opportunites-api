import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IDomaine } from '../domaine.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../domaine.test-samples';

import { DomaineService } from './domaine.service';

const requireRestSample: IDomaine = {
  ...sampleWithRequiredData,
};

describe('Domaine Service', () => {
  let service: DomaineService;
  let httpMock: HttpTestingController;
  let expectedResult: IDomaine | IDomaine[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(DomaineService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a Domaine', () => {
      const domaine = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(domaine).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Domaine', () => {
      const domaine = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(domaine).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Domaine', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Domaine', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Domaine', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addDomaineToCollectionIfMissing', () => {
      it('should add a Domaine to an empty array', () => {
        const domaine: IDomaine = sampleWithRequiredData;
        expectedResult = service.addDomaineToCollectionIfMissing([], domaine);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(domaine);
      });

      it('should not add a Domaine to an array that contains it', () => {
        const domaine: IDomaine = sampleWithRequiredData;
        const domaineCollection: IDomaine[] = [
          {
            ...domaine,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addDomaineToCollectionIfMissing(domaineCollection, domaine);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Domaine to an array that doesn't contain it", () => {
        const domaine: IDomaine = sampleWithRequiredData;
        const domaineCollection: IDomaine[] = [sampleWithPartialData];
        expectedResult = service.addDomaineToCollectionIfMissing(domaineCollection, domaine);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(domaine);
      });

      it('should add only unique Domaine to an array', () => {
        const domaineArray: IDomaine[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const domaineCollection: IDomaine[] = [sampleWithRequiredData];
        expectedResult = service.addDomaineToCollectionIfMissing(domaineCollection, ...domaineArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const domaine: IDomaine = sampleWithRequiredData;
        const domaine2: IDomaine = sampleWithPartialData;
        expectedResult = service.addDomaineToCollectionIfMissing([], domaine, domaine2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(domaine);
        expect(expectedResult).toContain(domaine2);
      });

      it('should accept null and undefined values', () => {
        const domaine: IDomaine = sampleWithRequiredData;
        expectedResult = service.addDomaineToCollectionIfMissing([], null, domaine, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(domaine);
      });

      it('should return initial array if no Domaine is added', () => {
        const domaineCollection: IDomaine[] = [sampleWithRequiredData];
        expectedResult = service.addDomaineToCollectionIfMissing(domaineCollection, undefined, null);
        expect(expectedResult).toEqual(domaineCollection);
      });
    });

    describe('compareDomaine', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareDomaine(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 14497 };
        const entity2 = null;

        const compareResult1 = service.compareDomaine(entity1, entity2);
        const compareResult2 = service.compareDomaine(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 14497 };
        const entity2 = { id: 24380 };

        const compareResult1 = service.compareDomaine(entity1, entity2);
        const compareResult2 = service.compareDomaine(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 14497 };
        const entity2 = { id: 14497 };

        const compareResult1 = service.compareDomaine(entity1, entity2);
        const compareResult2 = service.compareDomaine(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
