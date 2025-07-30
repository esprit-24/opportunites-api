package esprit.opportunites.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class VilleTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Ville getVilleSample1() {
        return new Ville().id(1L).nom("nom1");
    }

    public static Ville getVilleSample2() {
        return new Ville().id(2L).nom("nom2");
    }

    public static Ville getVilleRandomSampleGenerator() {
        return new Ville().id(longCount.incrementAndGet()).nom(UUID.randomUUID().toString());
    }
}
