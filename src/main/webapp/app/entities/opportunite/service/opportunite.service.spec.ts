import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IOpportunite } from '../opportunite.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../opportunite.test-samples';

import { OpportuniteService, RestOpportunite } from './opportunite.service';

const requireRestSample: RestOpportunite = {
  ...sampleWithRequiredData,
  dateDebut: sampleWithRequiredData.dateDebut?.toJSON(),
  dateFin: sampleWithRequiredData.dateFin?.toJSON(),
};

describe('Opportunite Service', () => {
  let service: OpportuniteService;
  let httpMock: HttpTestingController;
  let expectedResult: IOpportunite | IOpportunite[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(OpportuniteService);
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

    it('should create a Opportunite', () => {
      const opportunite = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(opportunite).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Opportunite', () => {
      const opportunite = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(opportunite).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Opportunite', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Opportunite', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Opportunite', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addOpportuniteToCollectionIfMissing', () => {
      it('should add a Opportunite to an empty array', () => {
        const opportunite: IOpportunite = sampleWithRequiredData;
        expectedResult = service.addOpportuniteToCollectionIfMissing([], opportunite);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(opportunite);
      });

      it('should not add a Opportunite to an array that contains it', () => {
        const opportunite: IOpportunite = sampleWithRequiredData;
        const opportuniteCollection: IOpportunite[] = [
          {
            ...opportunite,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addOpportuniteToCollectionIfMissing(opportuniteCollection, opportunite);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Opportunite to an array that doesn't contain it", () => {
        const opportunite: IOpportunite = sampleWithRequiredData;
        const opportuniteCollection: IOpportunite[] = [sampleWithPartialData];
        expectedResult = service.addOpportuniteToCollectionIfMissing(opportuniteCollection, opportunite);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(opportunite);
      });

      it('should add only unique Opportunite to an array', () => {
        const opportuniteArray: IOpportunite[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const opportuniteCollection: IOpportunite[] = [sampleWithRequiredData];
        expectedResult = service.addOpportuniteToCollectionIfMissing(opportuniteCollection, ...opportuniteArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const opportunite: IOpportunite = sampleWithRequiredData;
        const opportunite2: IOpportunite = sampleWithPartialData;
        expectedResult = service.addOpportuniteToCollectionIfMissing([], opportunite, opportunite2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(opportunite);
        expect(expectedResult).toContain(opportunite2);
      });

      it('should accept null and undefined values', () => {
        const opportunite: IOpportunite = sampleWithRequiredData;
        expectedResult = service.addOpportuniteToCollectionIfMissing([], null, opportunite, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(opportunite);
      });

      it('should return initial array if no Opportunite is added', () => {
        const opportuniteCollection: IOpportunite[] = [sampleWithRequiredData];
        expectedResult = service.addOpportuniteToCollectionIfMissing(opportuniteCollection, undefined, null);
        expect(expectedResult).toEqual(opportuniteCollection);
      });
    });

    describe('compareOpportunite', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareOpportunite(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 2855 };
        const entity2 = null;

        const compareResult1 = service.compareOpportunite(entity1, entity2);
        const compareResult2 = service.compareOpportunite(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 2855 };
        const entity2 = { id: 8585 };

        const compareResult1 = service.compareOpportunite(entity1, entity2);
        const compareResult2 = service.compareOpportunite(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 2855 };
        const entity2 = { id: 2855 };

        const compareResult1 = service.compareOpportunite(entity1, entity2);
        const compareResult2 = service.compareOpportunite(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
