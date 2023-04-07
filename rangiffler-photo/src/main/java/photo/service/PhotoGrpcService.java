package photo.service;

import guru.qa.grpc.niffler.grpc.*;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;
import photo.data.PhotoEntity;
import photo.data.repository.PhotoRepository;
import photo.service.api.GeoGrpcClient;

@GrpcService
public class PhotoGrpcService extends PhotoServiceGrpc.PhotoServiceImplBase {

    private final PhotoRepository photoRepository;
    private final GeoGrpcClient geoGrpcClient;

    @Autowired
    public PhotoGrpcService(PhotoRepository photoRepository, GeoGrpcClient geoGrpcClient) {
        this.photoRepository = photoRepository;
        this.geoGrpcClient = geoGrpcClient;
    }

    @Override
    public void addPhoto(Photo photo, StreamObserver<Photo> responseObserver) {
        PhotoEntity entity = photoRepository.save(PhotoEntity.fromGrpc(photo));
        Country country = geoGrpcClient.getCountryByCode(entity.getCountryCode());
        responseObserver.onNext(entity.toGrpcWithCountry(country));
        responseObserver.onCompleted();
    }

}
