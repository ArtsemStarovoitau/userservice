package by.starovoytov.userservice.mapper;

import by.starovoytov.userservice.dto.UserDto;
import by.starovoytov.userservice.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", uses = CardInfoMapper.class, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserMapper {
    User toEntity(UserDto dto);
    UserDto toDto(User entity);
    void updateUserFromDto(UserDto dto, @MappingTarget User entity);
}