package gateway.controller;

import java.util.List;
import java.util.Map;
import java.util.Set;

import gateway.model.PartnerStatus;
import gateway.model.UserDto;
import gateway.service.UserService;
import gateway.service.api.UserdataGrpcClient;
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

    private final UserdataGrpcClient userdataGrpcClient;

    @Autowired
    public UserController(UserService userService, RestUserdataClient restUserdataClient,
                          UserdataGrpcClient userdataGrpcClient) {
        this.userService = userService;
        this.restUserdataClient = restUserdataClient;
        this.userdataGrpcClient = userdataGrpcClient;
    }

    @GetMapping("/users")
    public Map<PartnerStatus, Set<UserDto>> getAllUsers(@AuthenticationPrincipal Jwt principal) {
        String username = principal.getClaim("sub");
        return userdataGrpcClient.getAllUsers(username);
    }

    @GetMapping("/currentUser")
    public UserDto getCurrentUser(@AuthenticationPrincipal Jwt principal) {
        String username = principal.getClaim("sub");
        return userdataGrpcClient.getCurrentUser(username);
    }

    @PatchMapping("/currentUser")
    public UserDto updateCurrentUser(@AuthenticationPrincipal Jwt principal,
                                     @Validated @RequestBody UserDto userDto) {
        String username = principal.getClaim("sub");
        userDto.setUsername(username);
        return userdataGrpcClient.updateCurrentUser(userDto);
    }




    // -----------------

    @GetMapping("/friends")
    public List<UserDto> getFriendsByUserId() {
        return userService.getFriends();
    }

    @GetMapping("invitations")
    public List<UserDto> getInvitations() {
        return userService.getInvitations();
    }

    @PostMapping("users/invite/")
    public void sendInvitation(@AuthenticationPrincipal Jwt principal,
                               @RequestBody UserDto user) {
        String username = principal.getClaim("sub");
        restUserdataClient.inviteToFriends(username, user);
    }

    @PostMapping("friends/remove")
    public void removeFriendFromUser(@AuthenticationPrincipal Jwt principal,
                                         @RequestBody UserDto friend) {
        String username = principal.getClaim("sub");
        restUserdataClient.removeFriend(username, friend);
    }

    @PostMapping("friends/submit")
    public void submitFriend(@AuthenticationPrincipal Jwt principal,
                             @RequestBody UserDto friend) {
        String username = principal.getClaim("sub");
        restUserdataClient.submitFriend(username, friend);
    }

    @PostMapping("friends/decline")
    public void declineFriend(@AuthenticationPrincipal Jwt principal,
                              @RequestBody UserDto friend) {
        String username = principal.getClaim("sub");
        restUserdataClient.declineFriend(username, friend);
    }

}
