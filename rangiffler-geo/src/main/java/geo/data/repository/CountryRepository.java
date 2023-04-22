package geo.data.repository;

import geo.data.CountryEntity;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface CountryRepository extends JpaRepository<CountryEntity, UUID> {

    @Nonnull
    List<CountryEntity> findAllByOrderByNameAsc();

    @Nullable
    CountryEntity findByCode(@Nonnull String code);

}
