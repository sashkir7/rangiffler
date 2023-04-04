package userdata.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Nonnull;
import lombok.*;
import userdata.data.UserEntity;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true, allowGetters = true)
public class UserDto {

    public static @Nonnull UserDto fromJson(@Nonnull String json) {
        try {
            return new ObjectMapper().readValue(json, UserDto.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static @Nonnull UserDto fromEntity(@Nonnull UserEntity entity) {
        byte[] photo = entity.getAvatar();
        return UserDto.builder()
                .id(entity.getId())
                .username(entity.getUsername())
                .firstname(entity.getFirstname())
                .lastname(entity.getLastname())
                .avatar(photo != null && photo.length > 0 ? new String(photo, StandardCharsets.UTF_8) : null)
                .build();
    }

    private UUID id;
    private String username,
            firstname,
            lastname,
            avatar;

}
