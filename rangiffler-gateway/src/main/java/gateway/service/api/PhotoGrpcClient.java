package gateway.service.api;

import gateway.model.CountryDto;
import gateway.model.PhotoDto;
import guru.qa.grpc.niffler.grpc.Country;
import guru.qa.grpc.niffler.grpc.Photo;
import guru.qa.grpc.niffler.grpc.PhotoServiceGrpc;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class PhotoGrpcClient {

    @GrpcClient("grpcPhotoClient")
    private PhotoServiceGrpc.PhotoServiceBlockingStub photoServiceBlockingStub;

    public PhotoDto addPhoto(PhotoDto photoDto) {
        Photo photo = photoServiceBlockingStub.addPhoto(convertPhotoDtoToGrpc(photoDto));
        return convertGrpcPhotoToDto(photo);
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
