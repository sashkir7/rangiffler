package userdata.data.repository;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.springframework.data.jpa.repository.JpaRepository;
import userdata.data.UserEntity;

import java.util.Set;
import java.util.UUID;

public interface UserRepository extends JpaRepository<UserEntity, UUID> {

    @Nullable
    UserEntity findByUsername(@Nonnull String username);

    @Nonnull
    Set<UserEntity> findAllByUsernameNot(@Nonnull String username);

}
