package com.adamkaz7.medicalclinic.service;

import com.adamkaz7.medicalclinic.command.CreateUserCommand;
import com.adamkaz7.medicalclinic.dto.UserDto;
import com.adamkaz7.medicalclinic.exception.user.UserAlreadyExistsException;
import com.adamkaz7.medicalclinic.exception.user.UserNotFoundException;
import com.adamkaz7.medicalclinic.exception.user.UserValidationException;
import com.adamkaz7.medicalclinic.mapper.UserMapper;
import com.adamkaz7.medicalclinic.model.User;
import com.adamkaz7.medicalclinic.repository.PatientRepository;
import com.adamkaz7.medicalclinic.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PatientRepository patientRepository;

    @Transactional
    public UserDto addUser(CreateUserCommand command) {
        if (userRepository.existsByEmail(command.email())) {
            throw new UserAlreadyExistsException(command.email());
        }
        log.info("Creating user account");
        User user = userMapper.toEntity(command);
        user = userRepository.save(user);
        return userMapper.toDto(user);
    }

    public UserDto getUserById(Long userId) {
        User user = findUserById(userId);
        return userMapper.toDto(user);
    }

    @Transactional
    public void deleteUserById(Long userId) {
        User user = findUserById(userId);
        if (patientRepository.existsByUser_Id(userId)) {
            throw new UserValidationException("Cannot delete user assigned to a patient");
        }
        log.info("Deleting user account: userId={}", userId);
        userRepository.delete(user);
    }

    @Transactional
    public void changeUserPassword(Long userId, String newPassword) {
        User user = findUserById(userId);
        log.info("Changing user password: userId={}", userId);
        user.changePassword(newPassword);
        userRepository.save(user);
    }

    private User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
    }
}
