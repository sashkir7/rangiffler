package jupiter.extension;

import data.jpa.EmfContext;
import jakarta.persistence.EntityManagerFactory;

public final class JpaExtension implements AroundAllTestsExtension {

    @Override
    public void afterAllTests() {
        EmfContext.INSTANCE.storedEmf()
                .forEach(EntityManagerFactory::close);
    }

}
