package com.adamkaz7.medicalclinic.validator;

import com.adamkaz7.medicalclinic.command.CreateUserCommand;
import com.adamkaz7.medicalclinic.exception.user.UserValidationException;
import org.springframework.stereotype.Component;

@Component
public class UserValidator {
    public void validateCreateUserCommand(CreateUserCommand command) {
        if (command == null) {
            throw new UserValidationException("User data is required");
        }
        validateUserData(
                command.email(),
                command.firstName(),
                command.lastName()
        );
        validatePassword(command.password());
    }

    private void validateUserData(String email, String firstName, String lastName) {
        validRequiredField(email, "Email");
        validRequiredField(firstName, "First name");
        validRequiredField(lastName, "Last name");
    }

    public void validatePassword(String password) {
        validRequiredField(password, "Password");
    }

    private void validRequiredField(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new UserValidationException(fieldName + " is required");
        }
    }
}
