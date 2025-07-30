package esprit.opportunites.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class OpportuniteTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Opportunite getOpportuniteSample1() {
        return new Opportunite().id(1L).titre("titre1").adresse("adresse1").nombrePostes(1);
    }

    public static Opportunite getOpportuniteSample2() {
        return new Opportunite().id(2L).titre("titre2").adresse("adresse2").nombrePostes(2);
    }

    public static Opportunite getOpportuniteRandomSampleGenerator() {
        return new Opportunite()
            .id(longCount.incrementAndGet())
            .titre(UUID.randomUUID().toString())
            .adresse(UUID.randomUUID().toString())
            .nombrePostes(intCount.incrementAndGet());
    }
}
