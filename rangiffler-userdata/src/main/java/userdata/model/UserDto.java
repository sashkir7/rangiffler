package userdata.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Nonnull;
import lombok.*;
import userdata.data.UserEntity;

import java.util.UUID;

import static java.nio.charset.StandardCharsets.UTF_8;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDto {

    public static @Nonnull UserDto fromJson(@Nonnull String json) {
        try {
            return new ObjectMapper().readValue(json, UserDto.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static @Nonnull UserDto fromEntity(@Nonnull UserEntity entity) {
        return UserDto.builder()
                .id(entity.getId())
                .username(entity.getUsername())
                .firstname(entity.getFirstname())
                .lastname(entity.getLastname())
                .avatar(entity.getAvatarAsString())
                .build();
    }

    private UUID id;
    private String username,
            firstname,
            lastname,
            avatar;

    @JsonIgnore
    public byte[] getAvatarAsBytes() {
        return avatar != null ? avatar.getBytes(UTF_8) : null;
    }

}
