package photo.data;

import guru.qa.grpc.niffler.grpc.Country;
import guru.qa.grpc.niffler.grpc.Photo;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.UUID;

import static java.nio.charset.StandardCharsets.UTF_8;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "photo")
@Accessors(chain = true)
public class PhotoEntity {

    public static PhotoEntity fromGrpc(Photo photo) {
        PhotoEntityBuilder builder = PhotoEntity.builder()
                .username(photo.getUsername())
                .description(photo.getDescription())
                .photo(photo.getPhoto().getBytes(UTF_8))
                .countryCode(photo.getCountry().getCode());
        if (!photo.getId().isBlank()) {
            builder.id(UUID.fromString(photo.getId()));
        }
        return builder.build();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false, columnDefinition = "UUID default gen_random_uuid()")
    private UUID id;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "description")
    private String description;

    @Column(name = "photo", columnDefinition = "bytea")
    private byte[] photo;

    @Column(name = "country_code", nullable = false)
    private String countryCode;

    public Photo toGrpcWithCountry(Country country) {
        Photo.Builder builder = Photo.newBuilder()
                .setUsername(username)
                .setDescription(description)
                .setCountry(country);
        if (id != null) {
            builder.setId(id.toString());
        }
        if (photo != null && photo.length > 0) {
            builder.setPhoto(new String(photo, UTF_8));
        }
        return builder.build();
    }

}
