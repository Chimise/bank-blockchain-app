package com.firstacademy.firstblock.service;

import com.firstacademy.firstblock.dto.mapper.UserMapper;
import com.firstacademy.firstblock.dto.model.UserDto;
import com.firstacademy.firstblock.exception.CustomExceptions;
import com.firstacademy.firstblock.exception.EntityType;
import com.firstacademy.firstblock.exception.ExceptionType;
import com.firstacademy.firstblock.model.Role;
import com.firstacademy.firstblock.model.User;
import com.firstacademy.firstblock.model.UserRoles;
import com.firstacademy.firstblock.repository.RoleRepository;
import com.firstacademy.firstblock.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;

import static com.firstacademy.firstblock.exception.EntityType.USER;
import static com.firstacademy.firstblock.exception.ExceptionType.DUPLICATE_ENTITY;
import static com.firstacademy.firstblock.exception.ExceptionType.ENTITY_NOT_FOUND;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private PasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDto signup(UserDto userDto) {
        User user = userRepository.findByEmail(userDto.getEmail());
        if (user == null) {
            user = new User()
                    .setFirstName(userDto.getFirstName())
                    .setLastName(userDto.getLastName())
                    .setPhoneNumber(userDto.getPhoneNumber())
                    .setPermanentAddress(userDto.getPermanentAddress())
                    .setDateOfBirth(userDto.getDateOfBirth())
                    .setPresentAddress(userDto.getPermanentAddress())
                    .setCity(userDto.getCity())
                    .setPostalCode(userDto.getPostalCode())
                    .setCountry(userDto.getCountry());

            return UserMapper.toUserDto(userRepository.save(user));
        }
        throw exception(USER, DUPLICATE_ENTITY, userDto.getEmail());
    }

    /**
     * Search an existing user
     *
     * @param email
     * @return
     */
    @Transactional
    public UserDto findUserByEmail(String email) {
        Optional<User> user = Optional.ofNullable(userRepository.findByEmail(email));
        if (user.isPresent()) {
            return UserMapper.toUserDto(user.get());
        }
        throw exception(USER, ENTITY_NOT_FOUND, email);
    }

    /**
     * Update User Profile
     *
     * @param userDto
     * @return
     */
    @Override
    public UserDto updateProfile(UserDto userDto) {
        Optional<User> user = Optional.ofNullable(userRepository.findByEmail(userDto.getEmail()));
        if (user.isPresent()) {
            User userModel = user.get();
            userModel.setFirstName(userDto.getFirstName())
                    .setLastName(userDto.getLastName())
                    .setPhoneNumber(userDto.getPhoneNumber())
                    .setPermanentAddress(userDto.getPermanentAddress())
                    .setDateOfBirth(userDto.getDateOfBirth())
                    .setPresentAddress(userDto.getPermanentAddress())
                    .setCity(userDto.getCity())
                    .setPostalCode(userDto.getPostalCode())
                    .setCountry(userDto.getCountry());

            return UserMapper.toUserDto(userRepository.save(userModel));
        }
        throw exception(USER, ENTITY_NOT_FOUND, userDto.getEmail());
    }

    /**
     * Change Password
     *
     * @param userDto
     * @param newPassword
     * @return
     */
    @Override
    public UserDto changePassword(UserDto userDto, String newPassword) {
        Optional<User> user = Optional.ofNullable(userRepository.findByEmail(userDto.getEmail()));
        if (user.isPresent()) {
            User userModel = user.get();
            userModel.setPassword(bCryptPasswordEncoder.encode(newPassword));
            return UserMapper.toUserDto(userRepository.save(userModel));
        }
        throw exception(USER, ENTITY_NOT_FOUND, userDto.getEmail());
    }

    @Override
    public UserDto getCurrentUser() throws UsernameNotFoundException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = (String) authentication.getPrincipal();
        if (email == null) {
            throw new UsernameNotFoundException("User details not found");
        }

        return this.findUserByEmail(email);
    }

    /**
     * Returns a new RuntimeException
     *
     * @param entityType
     * @param exceptionType
     * @param args
     * @return
     */
    private RuntimeException exception(EntityType entityType, ExceptionType exceptionType, String... args) {
        return CustomExceptions.throwException(entityType, exceptionType, args);
    }
}
