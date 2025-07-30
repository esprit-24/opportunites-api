package esprit.opportunites.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ProfilTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Profil getProfilSample1() {
        return new Profil().id(1L).intitule("intitule1");
    }

    public static Profil getProfilSample2() {
        return new Profil().id(2L).intitule("intitule2");
    }

    public static Profil getProfilRandomSampleGenerator() {
        return new Profil().id(longCount.incrementAndGet()).intitule(UUID.randomUUID().toString());
    }
}
