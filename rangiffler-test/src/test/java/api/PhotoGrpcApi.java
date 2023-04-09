package api;

import sashkir7.grpc.Photo;
import sashkir7.grpc.PhotoIdRequest;
import sashkir7.grpc.PhotoServiceGrpc;
import sashkir7.grpc.Photos;

public final class PhotoGrpcApi extends BaseGrpcApi {

    private final PhotoServiceGrpc.PhotoServiceBlockingStub blockingStub;

    public PhotoGrpcApi() {
        super("localhost", 9006);
        blockingStub = PhotoServiceGrpc.newBlockingStub(channel);
    }

    public Photos getUserPhotos(String username) {
        return blockingStub.getUserPhotos(getUsernameRequest(username));
    }

    public Photos getAllFriendsPhotos(String username) {
        return blockingStub.getAllFriendsPhotos(getUsernameRequest(username));
    }

    public Photo addPhoto(Photo photo) {
        return blockingStub.addPhoto(photo);
    }

    public Photo editPhoto(Photo photo) {
        return blockingStub.editPhoto(photo);
    }

    public void deletePhoto(String id) {
        blockingStub.deletePhoto(getPhotoIdRequest(id));
    }

    private PhotoIdRequest getPhotoIdRequest(String id) {
        return PhotoIdRequest.newBuilder().setId(id).build();
    }

}
