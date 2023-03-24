package org.rangiffler.controller;

import java.util.List;
import java.util.UUID;
import org.rangiffler.model.PhotoJson;
import org.rangiffler.service.PhotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PhotoController {


  private final PhotoService photoService;

  @Autowired
  public PhotoController(PhotoService photoService) {
    this.photoService = photoService;
  }


  @GetMapping("/photos")
  public List<PhotoJson> getPhotosForUser() {
    return photoService.getAllUserPhotos();
  }

  @GetMapping("/friends/photos")
  public List<PhotoJson> getAllFriendsPhotos() {
    return photoService.getAllFriendsPhotos();
  }

  @PostMapping("/photos")
  public PhotoJson addPhoto(@RequestBody PhotoJson photoJson) {
    return photoService.addPhoto(photoJson);
  }

  @PatchMapping("/photos/{id}")
  public PhotoJson editPhoto(@RequestBody PhotoJson photoJson) {
    return photoService.editPhoto(photoJson);
  }

  @DeleteMapping("/photos")
  public void deletePhoto(@RequestParam UUID photoId) {
    photoService.deletePhoto(photoId);
  }

}
