package gateway.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import sashkir7.grpc.User;
import sashkir7.grpc.Users;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDto {

    public static UserDto fromGrpc(User user) {
        return UserDto.builder()
                .id(UUID.fromString(user.getId()))
                .username(user.getUsername())
                .firstname(user.getFirstname())
                .lastname(user.getLastname())
                .avatar(user.getAvatar())
                .build();
    }

    public static Set<UserDto> fromGrpc(Users users) {
        return users.getUsersList().stream()
                .map(UserDto::fromGrpc)
                .collect(Collectors.toSet());
    }

    private UUID id;
    private String username, firstname, lastname, avatar;

    public User toGrpc() {
        User.Builder builder = User.newBuilder()
                .setUsername(username)
                .setFirstname(firstname)
                .setLastname(lastname);
        if (id != null) {
            builder.setId(id.toString());
        }
        if (avatar != null) {
            builder.setAvatar(avatar);
        }
        return builder.build();
    }

}

