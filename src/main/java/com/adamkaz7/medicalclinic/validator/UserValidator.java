package com.adamkaz7.medicalclinic.validator;

import com.adamkaz7.medicalclinic.command.CreateUserCommand;
import com.adamkaz7.medicalclinic.exception.UserValidationException;
import org.springframework.stereotype.Component;

@Component
public class UserValidator {
    public void validateCreateUserCommand(CreateUserCommand command) {
        if (command == null) {
            throw new UserValidationException("User data is required");
        }
        if (command.email() == null || command.email().isBlank()) {
            throw new UserValidationException("Email is required");
        }
        if (command.password() == null || command.password().isBlank()) {
            throw new UserValidationException("Password is required");
        }
        if (command.firstName() == null || command.firstName().isBlank()) {
            throw new UserValidationException("First name is required");
        }
        if (command.lastName() == null || command.lastName().isBlank()) {
            throw new UserValidationException("Last name is required");
        }
    }
}
