package com.adamkaz7.medicalclinic.service;

import com.adamkaz7.medicalclinic.command.CreateUserCommand;
import com.adamkaz7.medicalclinic.dto.UserDto;
import com.adamkaz7.medicalclinic.exception.PatientNotFoundException;
import com.adamkaz7.medicalclinic.exception.UserAlreadyExistsException;
import com.adamkaz7.medicalclinic.exception.UserNotFoundException;
import com.adamkaz7.medicalclinic.exception.UserValidationException;
import com.adamkaz7.medicalclinic.mapper.UserMapper;
import com.adamkaz7.medicalclinic.model.Patient;
import com.adamkaz7.medicalclinic.model.User;
import com.adamkaz7.medicalclinic.repository.PatientRepository;
import com.adamkaz7.medicalclinic.repository.UserRepository;
import com.adamkaz7.medicalclinic.validator.PatientValidator;
import com.adamkaz7.medicalclinic.validator.UserValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final UserValidator userValidator;
    private final PatientRepository patientRepository;
    private final PatientValidator patientValidator;

    public UserDto addUser(CreateUserCommand command) {
        userValidator.validateCreateUserCommand(command);
        if (userRepository.existsByEmail(command.email())) {
            throw new UserAlreadyExistsException(command.email());
        }
        User user = userMapper.toEntity(command);
        User savedUser = userRepository.save(user);
        return userMapper.toDto(savedUser);
    }

    public void deleteUserById(Long userId) {
        User user = findUserById(userId);
        if (patientRepository.existsByUser_Id(userId)) {
            throw new UserValidationException("Cannot delete user assigned to a patient");
        }
        userRepository.delete(user);
    }

    public void assignPatientToUser(Long userId, String email) {
        patientValidator.validateEmail(email);
        User user = findUserById(userId);
        Patient patient = patientRepository.findByEmail(email).orElseThrow(() -> new PatientNotFoundException(email));
        if (patient.getUser() != null) {
            throw new UserValidationException("Patient is already assigned to a user");
        }
        if (patientRepository.existsByUser_Id(userId)) {
            throw new UserValidationException("User is already assigned to a patient");
        }
        patient.assignUser(user);
        patientRepository.save(patient);
    }

    private User findUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
    }
}
