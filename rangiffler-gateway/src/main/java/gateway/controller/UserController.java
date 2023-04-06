package gateway.controller;

import java.util.List;
import java.util.Map;
import java.util.Set;

import gateway.model.FriendStatus;
import gateway.model.UserJson;
import gateway.service.UserService;
import gateway.service.api.GrpcFfasfasfAClient;
import gateway.service.api.RestUserdataClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    private final UserService userService;
    private final RestUserdataClient restUserdataClient;

    private final GrpcFfasfasfAClient grpcFfasfasfAClient;

    @Autowired
    public UserController(UserService userService, RestUserdataClient restUserdataClient,
                          GrpcFfasfasfAClient grpcFfasfasfAClient) {
        this.userService = userService;
        this.restUserdataClient = restUserdataClient;
        this.grpcFfasfasfAClient = grpcFfasfasfAClient;
    }

    @GetMapping("/users")
    public Map<FriendStatus, Set<UserJson>> getAllUsers(@AuthenticationPrincipal Jwt principal) {
        String username = principal.getClaim("sub");
        return restUserdataClient.getAllUsers(username);
    }

    @GetMapping("/currentUser")
    public UserJson getCurrentUser(@AuthenticationPrincipal Jwt principal) {
        String username = principal.getClaim("sub");
        return grpcFfasfasfAClient.getCurrentUser(username);
    }

    @PatchMapping("/currentUser")
    public UserJson updateCurrentUser(@AuthenticationPrincipal Jwt principal,
                                      @Validated @RequestBody UserJson user) {
        String username = principal.getClaim("sub");
        user.setUsername(username);
        return restUserdataClient.updateUserInfo(user);
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
    public void sendInvitation(@AuthenticationPrincipal Jwt principal,
                               @RequestBody UserJson user) {
        String username = principal.getClaim("sub");
        restUserdataClient.inviteToFriends(username, user);
    }

    @PostMapping("friends/remove")
    public void removeFriendFromUser(@AuthenticationPrincipal Jwt principal,
                                         @RequestBody UserJson friend) {
        String username = principal.getClaim("sub");
        restUserdataClient.removeFriend(username, friend);
    }

    @PostMapping("friends/submit")
    public void submitFriend(@AuthenticationPrincipal Jwt principal,
                             @RequestBody UserJson friend) {
        String username = principal.getClaim("sub");
        restUserdataClient.submitFriend(username, friend);
    }

    @PostMapping("friends/decline")
    public void declineFriend(@AuthenticationPrincipal Jwt principal,
                              @RequestBody UserJson friend) {
        String username = principal.getClaim("sub");
        restUserdataClient.declineFriend(username, friend);
    }

}
