package userdata.data;

import jakarta.persistence.*;
import lombok.*;
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
@ToString(exclude = {"avatar", "relationshipUsers"})
@EqualsAndHashCode(exclude = {"avatar", "relationshipUsers"})
public class UserEntity {

    public static UserEntity fromDto(UserDto userDto) {
        String photo = userDto.getAvatar();
        return UserEntity.builder()
                .id(userDto.getId())
                .username(userDto.getUsername())
                .firstname(userDto.getFirstname())
                .lastname(userDto.getLastname())
                .avatar(photo != null ? photo.getBytes(UTF_8) : null)
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
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<UsersRelationshipEntity> relationshipUsers = new HashSet<>();

    public Set<UserEntity> getRelationshipUsersByStatus(FriendStatus status) {
        return relationshipUsers.stream()
                .filter(user -> user.getRelationship() == status)
                .map(UsersRelationshipEntity::getFriend)
                .collect(Collectors.toSet());
    }

}