package gateway.controller;

import java.util.Set;
import java.util.UUID;

import gateway.model.PhotoDto;
import gateway.service.api.PhotoGrpcClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PhotoController {

    private final PhotoGrpcClient photoGrpcClient;

    @Autowired
    public PhotoController(PhotoGrpcClient photoGrpcClient) {
        this.photoGrpcClient = photoGrpcClient;
    }

    @GetMapping("/photos")
    public Set<PhotoDto> getPhotosForUser(@AuthenticationPrincipal Jwt principal) {
        String username = principal.getClaim("sub");
        return photoGrpcClient.getUserPhotos(username);
    }

    @GetMapping("/friends/photos")
    public Set<PhotoDto> getAllFriendsPhotos(@AuthenticationPrincipal Jwt principal) {
        String username = principal.getClaim("sub");
        return photoGrpcClient.getaAllFriendsPhotos(username);
    }

    @PostMapping("/photos")
    public PhotoDto addPhoto(@AuthenticationPrincipal Jwt principal,
                             @RequestBody PhotoDto photoDto) {
        String username = principal.getClaim("sub");
        photoDto.setUsername(username);
        return photoGrpcClient.addPhoto(photoDto);
    }

    @PatchMapping("/photos/{id}")
    public PhotoDto editPhoto(@RequestBody PhotoDto photoDto) {
        return photoGrpcClient.editPhoto(photoDto);
    }

    @DeleteMapping("/photos")
    public void deletePhoto(@RequestParam UUID photoId) {
        photoGrpcClient.deletePhoto(photoId);
    }

}
