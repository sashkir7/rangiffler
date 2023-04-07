package photo.data.repository;

import jakarta.annotation.Nonnull;
import org.springframework.data.jpa.repository.JpaRepository;
import photo.data.PhotoEntity;

import java.util.Collection;
import java.util.Set;
import java.util.UUID;

public interface PhotoRepository extends JpaRepository<PhotoEntity, UUID> {

    @Nonnull
    Set<PhotoEntity> findAllByUsername(@Nonnull String username);

    @Nonnull
    Set<PhotoEntity> findAllByUsernameIn(@Nonnull Collection<String> usernames);

}
