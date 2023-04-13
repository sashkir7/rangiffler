package gateway.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import sashkir7.grpc.RelationshipResponse;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class UsersRelationshipDto {

    public static UsersRelationshipDto fromGrpc(RelationshipResponse relationship) {
        return UsersRelationshipDto.builder()
                .user(UserDto.fromGrpc(relationship.getUser()))
                .partner(UserDto.fromGrpc(relationship.getPartner()))
                .status(PartnerStatus.valueOf(relationship.getStatus()))
                .build();
    }

    private UserDto user, partner;
    private PartnerStatus status;

}
