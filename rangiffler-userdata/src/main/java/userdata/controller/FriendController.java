package userdata.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import userdata.model.UserDto;
import userdata.service.UserService;

@RestController
public class FriendController {

    private final UserService userService;

    @Autowired
    public FriendController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("users/invite")
    public UserDto inviteToFriends(@RequestParam String username,
                                   @RequestBody UserDto user) {
        userService.inviteToFriends(username, user.getUsername());
        return null;
    }

    @PostMapping("friends/submit")
    public UserDto submitFriend(@RequestParam String username,
                                @RequestBody UserDto friend) {
        userService.submitFriend(username, friend);
        return null;
    }

    @PostMapping("friends/decline")
    public UserDto declineFriend(@RequestParam String username,
                                 @RequestBody UserDto friend) {
        userService.declineFriend(username, friend);
        return null;
    }

    @PostMapping("friends/remove")
    public UserDto removeFriend(@RequestParam String username,
                                @RequestBody UserDto friend) {
        userService.removeFriend(username, friend);
        return null;
    }

}
