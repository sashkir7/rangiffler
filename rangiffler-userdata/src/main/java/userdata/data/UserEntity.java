package userdata.data;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;
import userdata.model.UserDto;

import java.util.*;
import java.util.stream.Collectors;

import static java.nio.charset.StandardCharsets.UTF_8;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
@Accessors(chain = true)
@ToString(exclude = {"avatar", "relationshipUsers"})
@EqualsAndHashCode(exclude = {"avatar", "relationshipUsers"})
public class UserEntity {

    public static UserEntity fromDto(UserDto userDto) {
        return UserEntity.builder()
                .id(userDto.getId())
                .username(userDto.getUsername())
                .firstname(userDto.getFirstname())
                .lastname(userDto.getLastname())
                .avatar(userDto.getAvatarAsBytes())
                .build();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false, columnDefinition = "UUID default gen_random_uuid()")
    private UUID id;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "firstname", nullable = false)
    private String firstname;

    @Column(name = "lastname", nullable = false)
    private String lastname;

    @Column(name = "avatar", columnDefinition = "bytea")
    private byte[] avatar;

    @Builder.Default
    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<UsersRelationshipEntity> relationshipUsers = new HashSet<>();

    public void addUserRelationship(UsersRelationshipEntity relationshipEntity) {
        relationshipUsers.add(relationshipEntity);
    }

    public void removeRelationship(UsersRelationshipEntity relationshipEntity) {
        relationshipUsers.remove(relationshipEntity);
    }

    public Set<UserEntity> getRelationshipUsersByStatus(PartnerStatus status) {
        return relationshipUsers.stream()
                .filter(user -> user.getStatus() == status)
                .map(UsersRelationshipEntity::getPartner)
                .collect(Collectors.toSet());
    }

    public Optional<UsersRelationshipEntity> findRelationship(UserEntity partner, PartnerStatus status) {
        return relationshipUsers.stream()
                .filter(rel -> rel.getPartner().getUsername().equals(partner.getUsername()))
                .filter(rel -> status == null || rel.getStatus().equals(status))
                .findFirst();
    }

    public Optional<UsersRelationshipEntity> findRelationship(UserEntity partner) {
        return findRelationship(partner, null);
    }

    public String getAvatarAsString() {
        return avatar != null && avatar.length > 0 ? new String(avatar, UTF_8) : null;
    }

}