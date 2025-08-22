package com.Ejada.UserService.servieces.impl;

import com.Ejada.UserService.domain.dto.UserDto;
import com.Ejada.UserService.domain.dto.UserLogin;
import com.Ejada.UserService.domain.dto.responses.UserProfileResponse;
import com.Ejada.UserService.domain.entities.UserEntity;
import com.Ejada.UserService.exceptions.RegisterException;
import com.Ejada.UserService.exceptions.UserLoginException;
import com.Ejada.UserService.exceptions.UserNotFoundException;
import com.Ejada.UserService.mappers.UserMapper;
import com.Ejada.UserService.repositories.UserRepository;
import com.Ejada.UserService.servieces.UserService;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/** userServiceImpl */
@Service
public class UserServiceImpl implements UserService {
  @Autowired private UserRepository userRepository;
  @Autowired private UserMapper userMapper;
  @Autowired private PasswordEncoder passwordEncoder;

  private boolean isEmailUsed(String email) {
    Optional<UserEntity> account = userRepository.findByEmail(email);
    return account.isPresent();
  }

  private boolean isUserNameUsed(String userName) {
    Optional<UserEntity> account = userRepository.findByUsername(userName);
    return account.isPresent();
  }

  @Override
  public String registerUser(UserDto userDto) {
    if (isEmailUsed(userDto.getEmail()) || isUserNameUsed(userDto.getUsername())) {
      throw new RegisterException();
    }
    UserEntity user = userMapper.dtoToEntity(userDto);
    user.setPassword(passwordEncoder.encode(userDto.getPassword()));
    UserEntity savedUser = userRepository.save(user);
    return savedUser.getUserId().toString();
  }

  @Override
  public String loginUser(UserLogin userLogin) {
    Optional<UserEntity> account = userRepository.findByUsername(userLogin.getUsername());
    if (account.isPresent()) {
      if (!passwordMatcher(userLogin.getPassword(), account.get().getPassword())) {
        throw new UserLoginException();
      } else return account.get().getUserId().toString();
    } else {
      throw new UserLoginException();
    }
  }

  @Override
  public UserProfileResponse getUserProfile(UUID userId) {
    Optional<UserEntity> user = userRepository.findById(userId);
    if (user.isEmpty()) {
      throw new UserNotFoundException("User with ID: " + userId + " Not Found");
    } else {
      return userMapper.entityToProfileResponse(user.get());
    }
  }

  private boolean passwordMatcher(String password, String hashedPassword) {
    return passwordEncoder.matches(password, hashedPassword);
  }
}
