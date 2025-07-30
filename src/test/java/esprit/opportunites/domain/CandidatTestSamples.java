package esprit.opportunites.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class CandidatTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Candidat getCandidatSample1() {
        return new Candidat().id(1L).cvUrl("cvUrl1").statutActuel("statutActuel1");
    }

    public static Candidat getCandidatSample2() {
        return new Candidat().id(2L).cvUrl("cvUrl2").statutActuel("statutActuel2");
    }

    public static Candidat getCandidatRandomSampleGenerator() {
        return new Candidat()
            .id(longCount.incrementAndGet())
            .cvUrl(UUID.randomUUID().toString())
            .statutActuel(UUID.randomUUID().toString());
    }
}
