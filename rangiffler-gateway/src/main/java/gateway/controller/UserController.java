package gateway.controller;

import java.util.List;
import java.util.Map;

import gateway.model.FriendStatus;
import gateway.model.UserJson;
import gateway.service.UserService;
import gateway.service.api.RestUserdataClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    private final UserService userService;
    private final RestUserdataClient restUserdataClient;

    @Autowired
    public UserController(UserService userService, RestUserdataClient restUserdataClient) {
        this.userService = userService;
        this.restUserdataClient = restUserdataClient;
    }

    @GetMapping("/users")
    public Map<FriendStatus, List<UserJson>> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/currentUser")
    public UserJson getCurrentUser(@AuthenticationPrincipal Jwt principal) {
        String username = principal.getClaim("sub");
        return restUserdataClient.currentUser(username);
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
