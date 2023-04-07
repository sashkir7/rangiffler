package gateway.service.api;

import gateway.model.PhotoDto;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Component;
import sashkir7.grpc.*;

import java.util.Set;
import java.util.UUID;

@Component
public class PhotoGrpcClient {

    @GrpcClient("grpcPhotoClient")
    private PhotoServiceGrpc.PhotoServiceBlockingStub photoServiceBlockingStub;

    public PhotoDto addPhoto(PhotoDto photoDto) {
        Photo photo = photoServiceBlockingStub.addPhoto(photoDto.toGrpc());
        return PhotoDto.fromGrpc(photo);
    }

    public PhotoDto editPhoto(PhotoDto photoDto) {
        Photo photo = photoServiceBlockingStub.editPhoto(photoDto.toGrpc());
        return PhotoDto.fromGrpc(photo);
    }

    public void deletePhoto(UUID id) {
        photoServiceBlockingStub.deletePhoto(getPhotoIdRequest(id));
    }

    public Set<PhotoDto> getUserPhotos(String username) {
        Photos photos = photoServiceBlockingStub.getUserPhotos(getUsernameRequest(username));
        return PhotoDto.fromGrpc(photos);
    }

    public Set<PhotoDto> getaAllFriendsPhotos(String username) {
        Photos photos = photoServiceBlockingStub.getAllFriendsPhotos(getUsernameRequest(username));
        return PhotoDto.fromGrpc(photos);
    }

    private PhotoIdRequest getPhotoIdRequest(UUID id) {
        return PhotoIdRequest.newBuilder().setId(id.toString()).build();
    }

    private UsernameRequest getUsernameRequest(String username) {
        return UsernameRequest.newBuilder().setUsername(username).build();
    }

}
