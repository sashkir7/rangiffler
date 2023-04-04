package userdata.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import userdata.model.UserDto;
import userdata.service.UserService;

@RestController
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/currentUser")
    public UserDto getCurrentUser(String username) {
        // ToDo Обработать исключения: пользователь не найден или query не передали
        return userService.getCurrentUser(username);
    }

}
