package com.example.backend.mapper;

import com.example.backend.dto.UserDTO;
import com.example.backend.entity.User;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    
    UserDTO toDTO(User user);
    
    List<UserDTO> toDTOList(List<User> users);
    
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "accountNonExpired", constant = "true")
    @Mapping(target = "accountNonLocked", constant = "true")
    @Mapping(target = "credentialsNonExpired", constant = "true")
    User toEntity(UserDTO userDTO);
    
    @Mapping(target = "password", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDTO(UserDTO userDTO, @MappingTarget User user);
} 