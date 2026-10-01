package com.tasktracker.user.mapper;

import com.tasktracker.user.dto.UserResponse;
import com.tasktracker.user.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(target = "roleName", expression = "java(user.getRole() != null ? user.getRole().getDisplayName() : null)")
    @Mapping(target = "subRoleName", expression = "java(user.getSubRole() != null ? user.getSubRole().getDisplayName() : null)")
    @Mapping(target = "statusName", expression = "java(user.getStatus() != null ? user.getStatus().getDisplayName() : null)")
    UserResponse toResponse(User user);
}