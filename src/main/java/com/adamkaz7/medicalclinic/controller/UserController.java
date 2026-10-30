package com.adamkaz7.medicalclinic.controller;

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

    @PatchMapping("/{userId}/patient/{email}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void assignPatientToUser(@PathVariable Long userId, @PathVariable String email) {
        userService.assignPatientToUser(userId, email);
    }
}
