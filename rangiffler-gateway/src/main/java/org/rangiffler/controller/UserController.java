package org.rangiffler.controller;

import java.util.List;
import org.rangiffler.model.UserJson;
import org.rangiffler.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

  private final UserService userService;

  @Autowired
  public UserController(UserService userService) {
    this.userService = userService;
  }

  @GetMapping("/users")
  public List<UserJson> getAllUsers() {
    return userService.getAllUsers();
  }

  @GetMapping("/currentUser")
  public UserJson getCurrentUser() {
    return userService.getCurrentUser();
  }

  @PatchMapping("/currentUser")
  public UserJson updateCurrentUser(@RequestBody UserJson user) {
    return userService.updateCurrentUser(user);
  }

  @GetMapping("/friends")
  public List<UserJson> getFriendsByUserId() {
    return userService.getFriends();
  }

  @GetMapping("invitations")
  public List<UserJson> getInvitations() {
    return userService.getInvitations();
  }

  @PostMapping("users/invite/")
  public UserJson sendInvitation(@RequestBody UserJson user) {
    return userService.sendInvitation(user);
  }

  @PostMapping("friends/remove")
  public UserJson removeFriendFromUser(@RequestBody UserJson friend) {
    return userService.removeUserFromFriends(friend);
  }

  @PostMapping("friends/submit")
  public UserJson submitFriend(@RequestBody UserJson friend) {
    return userService.acceptInvitation(friend);
  }

  @PostMapping("friends/decline")
  public UserJson declineFriend(@RequestBody UserJson friend) {
    return userService.declineInvitation(friend);
  }

}
