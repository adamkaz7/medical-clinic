package com.adamkaz7.medicalclinic.controller;

import com.adamkaz7.medicalclinic.command.ChangeUserPasswordCommand;
import com.adamkaz7.medicalclinic.command.CreateUserCommand;
import com.adamkaz7.medicalclinic.dto.UserDto;
import com.adamkaz7.medicalclinic.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/{userId}")
    public UserDto getUserById(@PathVariable Long userId) {
        return userService.getUserById(userId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto addUser(@RequestBody CreateUserCommand command) {
        return userService.addUser(command);
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable Long userId) {
        userService.deleteUserById(userId);
    }

    @PatchMapping("/{userId}/password")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void changeUserPassword(@PathVariable Long userId, @RequestBody ChangeUserPasswordCommand command) {
        userService.changeUserPassword(userId, command.password());
    }
}
