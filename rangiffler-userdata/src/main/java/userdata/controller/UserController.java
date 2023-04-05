package userdata.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import userdata.data.FriendStatus;
import userdata.model.UserDto;
import userdata.service.UserService;

import java.util.Map;
import java.util.Set;

@RestController
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/allUsers")
    public Map<FriendStatus, Set<UserDto>> getAllUsers(@RequestParam String username) {
        // ToDo Также добавить обработку исключений...
        return userService.getAllUsers(username);
    }

    @GetMapping("/currentUser")
    public UserDto getCurrentUser(@RequestParam String username) {
        // ToDo Обработать исключения: пользователь не найден или query не передали
        return userService.getCurrentUser(username);
    }

    @PostMapping("/currentUser")
    public UserDto updateCurrentUser(@RequestBody UserDto user) {
        return userService.updateCurrentUser(user);
    }

}
