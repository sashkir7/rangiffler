package userdata.data.repository;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import userdata.data.UserEntity;

import java.util.Set;
import java.util.UUID;

public interface UserRepository extends JpaRepository<UserEntity, UUID> {

    @Nullable
    UserEntity findByUsername(@Nonnull String username);

    @Nonnull
    Set<UserEntity> findAllByUsernameNot(@Nonnull String username);

    @Modifying
    @Transactional
    @Query("DELETE FROM UsersRelationshipEntity u WHERE u.partner.id = :id")
    void deleteAllByRelationshipsUsersWherePartnerId(@Nonnull @Param("id") UUID id);

}
