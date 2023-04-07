package gateway.model;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import lombok.Builder;
import lombok.Data;
import sashkir7.grpc.Photo;
import sashkir7.grpc.Photos;

@Data
@Builder
public class PhotoDto {

    public static PhotoDto fromGrpc(Photo photo) {
        return PhotoDto.builder()
                .id(UUID.fromString(photo.getId()))
                .username(photo.getUsername())
                .description(photo.getDescription())
                .photo(photo.getPhoto())
                .country(CountryDto.fromGrpc(photo.getCountry()))
                .build();
    }

    public static Set<PhotoDto> fromGrpc(Photos photos) {
        return photos.getPhotosList().stream()
                .map(PhotoDto::fromGrpc)
                .collect(Collectors.toSet());
    }

    private UUID id;
    private String username, description, photo;
    private CountryDto country;

    public Photo toGrpc() {
        Photo.Builder builder = Photo.newBuilder()
                .setUsername(username)
                .setCountry(country.toGrpc());
        if (id != null && !id.toString().isBlank()) {
            builder.setId(id.toString());
        }
        if (description != null) {
            builder.setDescription(description);
        }
        if (photo != null) {
            builder.setPhoto(photo);
        }
        return builder.build();
    }

}
