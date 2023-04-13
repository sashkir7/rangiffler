package gateway.controller;

import java.util.Map;
import java.util.Set;

import gateway.model.PartnerStatus;
import gateway.model.UserDto;
import gateway.model.UsersRelationshipDto;
import gateway.service.api.UserdataGrpcClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    private final UserdataGrpcClient userdataGrpcClient;

    @Autowired
    public UserController(UserdataGrpcClient userdataGrpcClient) {
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

    @GetMapping("/friends")
    public Set<UserDto> getFriends(@AuthenticationPrincipal Jwt principal) {
        String username = principal.getClaim("sub");
        return userdataGrpcClient.getFriends(username);
    }

    @PostMapping("friends/invite")
    public UsersRelationshipDto sendInvitation(@AuthenticationPrincipal Jwt principal,
                                               @RequestBody UserDto partnerDto) {
        String username = principal.getClaim("sub");
        return userdataGrpcClient.inviteToFriends(username, partnerDto);
    }

    @PostMapping("friends/submit")
    public UsersRelationshipDto submitFriend(@AuthenticationPrincipal Jwt principal,
                                             @RequestBody UserDto partnerDto) {
        String username = principal.getClaim("sub");
        return userdataGrpcClient.submitFriends(username, partnerDto);
    }

    @PostMapping("friends/decline")
    public void declineFriend(@AuthenticationPrincipal Jwt principal,
                              @RequestBody UserDto partnerDto) {
        String username = principal.getClaim("sub");
        userdataGrpcClient.declineFriend(username, partnerDto);
    }

    @PostMapping("friends/remove")
    public void removeFriendFromUser(@AuthenticationPrincipal Jwt principal,
                                     @RequestBody UserDto partnerDto) {
        String username = principal.getClaim("sub");
        userdataGrpcClient.removeFriend(username, partnerDto);
    }

}
