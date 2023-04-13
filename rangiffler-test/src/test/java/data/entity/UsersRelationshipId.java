package data.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.UUID;

@Data
public class UsersRelationshipId implements Serializable {

    private UUID user, partner;

}
