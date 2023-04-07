package gateway.service.api;

import gateway.model.CountryDto;
import gateway.model.PhotoDto;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Component;
import sashkir7.grpc.*;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class PhotoGrpcClient {

    @GrpcClient("grpcPhotoClient")
    private PhotoServiceGrpc.PhotoServiceBlockingStub photoServiceBlockingStub;

    public PhotoDto addPhoto(PhotoDto photoDto) {
        Photo photo = photoServiceBlockingStub.addPhoto(convertPhotoDtoToGrpc(photoDto));
        return convertGrpcPhotoToDto(photo);
    }

    public PhotoDto editPhoto(PhotoDto photoDto) {
        Photo photo = photoServiceBlockingStub.editPhoto(convertPhotoDtoToGrpc(photoDto));
        return convertGrpcPhotoToDto(photo);
    }

    public void deletePhoto(String id) {
        PhotoIdRequest request = PhotoIdRequest.newBuilder().setId(id).build();
        photoServiceBlockingStub.deletePhoto(request);
    }

    public Set<PhotoDto> getUserPhotos(String username) {
        UsernameRequest request = UsernameRequest.newBuilder().setUsername(username).build();
        Photos userPhotos = photoServiceBlockingStub.getUserPhotos(request);
        return userPhotos.getPhotosList().stream().map(this::convertGrpcPhotoToDto).collect(Collectors.toSet());
    }

    public Set<PhotoDto> getaAllFriendsPhotos(String username) {
        UsernameRequest request = UsernameRequest.newBuilder().setUsername(username).build();
        Photos userPhotos = photoServiceBlockingStub.getAllFriendsPhotos(request);
        return userPhotos.getPhotosList().stream().map(this::convertGrpcPhotoToDto).collect(Collectors.toSet());
    }

    private PhotoDto convertGrpcPhotoToDto(Photo photo) {
        return PhotoDto.builder()
                .id(UUID.fromString(photo.getId()))
                .username(photo.getUsername())
                .description(photo.getDescription())
                .photo(photo.getPhoto())
                .countryDto(convertGrpcCountryToDto(photo.getCountry()))
                .build();
    }

    private Photo convertPhotoDtoToGrpc(PhotoDto photoDto) {
        Photo.Builder builder = Photo.newBuilder()
                .setUsername(photoDto.getUsername())
                .setPhoto(photoDto.getPhoto())
                .setCountry(convertCountryDtoToGrpc(photoDto.getCountryDto()));

        if (photoDto.getId() != null && !photoDto.getId().toString().isBlank()) {
            builder.setId(photoDto.getId().toString());
        }
        if (photoDto.getDescription() != null) {
            builder.setDescription(photoDto.getDescription());
        }
        return builder.build();
    }

    private CountryDto convertGrpcCountryToDto(Country country) {
        return CountryDto.builder()
                .id(UUID.fromString(country.getId()))
                .code(country.getCode())
                .name(country.getName())
                .build();
    }

    private Country convertCountryDtoToGrpc(CountryDto countryDto) {
        return Country.newBuilder()
                .setId(countryDto.getId().toString())
                .setCode(countryDto.getCode())
                .setName(countryDto.getName())
                .build();
    }

}
