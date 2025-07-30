package esprit.opportunites.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class OrganisationTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Organisation getOrganisationSample1() {
        return new Organisation()
            .id(1L)
            .nom("nom1")
            .secteurActivite("secteurActivite1")
            .logoUrl("logoUrl1")
            .adresse("adresse1")
            .siteWeb("siteWeb1")
            .emailContact("emailContact1")
            .telephone("telephone1");
    }

    public static Organisation getOrganisationSample2() {
        return new Organisation()
            .id(2L)
            .nom("nom2")
            .secteurActivite("secteurActivite2")
            .logoUrl("logoUrl2")
            .adresse("adresse2")
            .siteWeb("siteWeb2")
            .emailContact("emailContact2")
            .telephone("telephone2");
    }

    public static Organisation getOrganisationRandomSampleGenerator() {
        return new Organisation()
            .id(longCount.incrementAndGet())
            .nom(UUID.randomUUID().toString())
            .secteurActivite(UUID.randomUUID().toString())
            .logoUrl(UUID.randomUUID().toString())
            .adresse(UUID.randomUUID().toString())
            .siteWeb(UUID.randomUUID().toString())
            .emailContact(UUID.randomUUID().toString())
            .telephone(UUID.randomUUID().toString());
    }
}
