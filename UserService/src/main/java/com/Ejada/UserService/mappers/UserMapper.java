package com.Ejada.UserService.mappers;

import com.Ejada.UserService.domain.dto.UserDto;
import com.Ejada.UserService.domain.dto.responses.UserProfileResponse;
import com.Ejada.UserService.domain.entities.UserEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
  UserEntity dtoToEntity(UserDto userDto);

  UserDto entityToDto(UserEntity userEntity);

  UserProfileResponse entityToProfileResponse(UserEntity userEntity);
}
