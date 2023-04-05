package userdata.data;

import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users_relationship")
public class UsersRelationshipEntity {

    @Id
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private UserEntity user;

    @Id
    @ManyToOne
    @JoinColumn(name = "friend_id", referencedColumnName = "id", nullable = false)
    private UserEntity friend;

    @Column(name = "relationship", nullable = false)
    @Enumerated(EnumType.STRING)
    private FriendStatus relationship;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UsersRelationshipEntity that = (UsersRelationshipEntity) o;
        return Objects.equals(user.getId(), that.user.getId())
                && Objects.equals(friend.getId(), that.friend.getId())
                && relationship == that.relationship;
    }

    @Override
    public int hashCode() {
        return Objects.hash(user.getId(), friend.getId(), relationship);
    }

}
