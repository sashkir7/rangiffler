package userdata.data;

import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@IdClass(UsersRelationshipId.class)
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

}
