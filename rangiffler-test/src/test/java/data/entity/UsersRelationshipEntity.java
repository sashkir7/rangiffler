package data.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import model.PartnerStatus;

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
    @JoinColumn(name = "partner_id", referencedColumnName = "id", nullable = false)
    private UserEntity partner;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private PartnerStatus status;

}
